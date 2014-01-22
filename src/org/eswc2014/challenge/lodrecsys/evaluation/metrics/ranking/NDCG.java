/**
 * NDCG.java
 *
 * Computation of the normalized Discounted Cumulative Gain (nDCG) value on a
 * set of real and predicted test scores (ratings) from a recommendation method.
 *
 * For details about the nDCG formula, see:
 *
 * Jarvelin, K., Kekalainen, J. 2002. Cumulated Gain-based Evaluation of IR
 * Techniques. ACM Transactions on Information Systems 20(4), 422–446.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.ranking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;
import org.eswc2014.challenge.lodrecsys.evaluation.relevance.ScoreRelevance;

public class NDCG implements Metric {

    private ScoreRelevance scoreRelevance;

    /**
     * Builds a NDCG instance considering a given function to distinguish
     * relevant (equal or greater than) and non relevant (lower than) items for
     * the users.
     *
     * @param scoreRelevance an object that determines if a particular score
     * represents a relevant or a non relevant item
     */
    public NDCG(ScoreRelevance scoreRelevance) throws IllegalArgumentException {
        if (scoreRelevance == null) {
            throw new IllegalArgumentException("null score relenvance function");
        }

        this.scoreRelevance = scoreRelevance;
    }

    /**
     * Computes the nDCG value on the real and predicted test scores given as
     * input arguments.
     *
     * @param scores test scores with which compute the nDCG value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        return this.evaluate(scores, Integer.MAX_VALUE);
    }

    /**
     * Computes the nDCG value on the real and predicted test scores given as
     * input arguments, only considering the k highest test scores of each user.
     *
     * @param scores test scores with which compute the nDCG value
     * @param k the number of highest test scores of each user with which
     * compute the nDCG value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores, int k) throws Exception {
        if (scores == null) {
            throw new IllegalArgumentException("null score set");
        }
        if (k <= 0) {
            throw new IllegalArgumentException("k <= 0");
        }

        Map<String, Map<String, Score>> realScores = scores.getRealTestScoreMap();
        Map<String, Map<String, Score>> predictedScores = scores.getPredictedTestScoreMap();

        double ndcg = 0.0;

        int nUsers = realScores.size();

        for (String user : realScores.keySet()) {

            List<Score> userRealScores = new ArrayList<Score>(realScores.get(user).values());
            Collections.sort(userRealScores);

            Map<String, Double> userItemRelevances = new HashMap<String, Double>();
            for (int i = 0; i < userRealScores.size(); i++) {
                String item = userRealScores.get(i).getItemId();
                double itemRelevance = this.scoreRelevance.relevanceOf(userRealScores.get(i).getValue());
                userItemRelevances.put(item, itemRelevance);
            }

            double idcg = 0.0;
            for (int i = 0; i < userRealScores.size() && i < k; i++) {
                double log2 = Math.log((i + 1) + 1) / Math.log(2);
                idcg += userItemRelevances.get(userRealScores.get(i).getItemId()) / log2;
            }

            if (idcg == 0) {
                nUsers--;
                continue;
            }

            List<Score> userPredictedScores = new ArrayList<Score>(predictedScores.get(user).values());
            Collections.sort(userPredictedScores);

            double dcg = 0.0;
            for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                double log2 = Math.log((i + 1) + 1) / Math.log(2);
                dcg += userItemRelevances.get(userPredictedScores.get(i).getItemId()) / log2;
            }

            ndcg += dcg / idcg;
        }

        ndcg /= nUsers;
        return ndcg;
    }
}
