/**
 * Recall.java
 *
 * Computation of the recall value on a set of real and predicted test scores
 * (ratings) from a recommendation method.
 *
 * For details about the recall formula, see:
 *
 * Ricci, F., Rokach, L., Shapira, B., Kantor, P. B. 2011. Recommender Systems
 * Handbook. ISBN 978-0-387-85819-7.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction;

import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;

public class Recall implements Metric {

    private double relevanceScoreThreshold;

    /**
     * Builds a Recall instance considering the given threshold score value to
     * distinguish relevant (equal or greater than) and non relevant (lower
     * than) items for the users.
     *
     * @param relevanceScoreThreshold the threshold score value to distinguish
     * relevant and non relevant items
     */
    public Recall(double relevanceScoreThreshold) throws IllegalArgumentException {
        this.relevanceScoreThreshold = relevanceScoreThreshold;
    }

    /**
     * Computes the recall value on the real and predicted test scores given as
     * input arguments.
     *
     * @param scores test scores with which compute the recall value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        return this.evaluate(scores, Integer.MAX_VALUE);
    }

    /**
     * Computes the recall value on the real and predicted test scores given as
     * input arguments, only considering the k highest test scores of each user.
     *
     * @param scores test scores with which compute the recall value
     * @param k the number of highest test scores of each user with which
     * compute the recall value
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

        double r = 0.0;

        int nUsers = realScores.size();

        for (String user : realScores.keySet()) {

            List<String> userRealRelevantItems = new ArrayList<String>();
            for (String item : realScores.get(user).keySet()) {
                boolean isRelevant = realScores.get(user).get(item).getValue() >= this.relevanceScoreThreshold ? true : false;
                if (isRelevant) {
                    userRealRelevantItems.add(item);
                }
            }
            int nUserRealRelevantItems = userRealRelevantItems.size();

            List<Score> userPredictedScores = new ArrayList<Score>(predictedScores.get(user).values());
            Collections.sort(userPredictedScores);
            List<String> userPredictedRelevantItems = new ArrayList<String>();
            for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                boolean isRelevant = userPredictedScores.get(i).getValue() >= this.relevanceScoreThreshold ? true : false;
                if (isRelevant) {
                    userPredictedRelevantItems.add(userPredictedScores.get(i).getItemId());
                }
            }
            //int nUserPredictedRelevantItems = userPredictedRelevantItems.size();

            int nIntersectionRelevantItems = 0;
            for (String item : userRealRelevantItems) {
                if (userPredictedRelevantItems.contains(item)) {
                    nIntersectionRelevantItems++;
                }
            }

            if (nUserRealRelevantItems > 0) {
                r += (double) nIntersectionRelevantItems / nUserRealRelevantItems;
            } else {
                nUsers--;
            }
        }

        r /= nUsers;

        return r;
    }
}
