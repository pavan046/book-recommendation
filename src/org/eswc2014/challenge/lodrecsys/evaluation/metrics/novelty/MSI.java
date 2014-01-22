/**
 * MSI.java
 *
 * Computation of the normalized Mean Self-Information (MSI) novely value on a
 * set of predicted test scores (ratings) from a recommendation method.
 *
 * For details about the MSI formula, see:
 *
 * Zhou, T., Kuscsik, Z., Liu, J.-G., Medo, M., Wakeling, J. R., Zhang, Y.-C.
 * 2010. Solving the Apparent Diversity-accuracy Dilemma of Recommender Systems.
 * In Proceedings of the National Academy of Sciences of the United States of
 * America 170(10), pp. 4511-4515.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.novelty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;

public class MSI implements Metric {

    private double relevanceScoreThreshold;

    /**
     * Builds a MSI instance considering the given threshold score value to
     * distinguish relevant (equal or greater than) and non relevant (lower
     * than) items for the users.
     *
     * @param relevanceScoreThreshold the threshold score value to distinguish
     * relevant and non relevant items
     */
    public MSI(double relevanceScoreThreshold) throws IllegalArgumentException {
        this.relevanceScoreThreshold = relevanceScoreThreshold;
    }

    /**
     * Computes the MSI value on the predicted test scores given as input
     * arguments.
     *
     * @param scores test scores with which compute the MSI value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        return this.evaluate(scores, Integer.MAX_VALUE);
    }

    /**
     * Computes the MSI value on the predicted test scores given as input
     * arguments, only considering the k highest test scores of each user.
     *
     * @param scores test scores with which compute the MSI value
     * @param k the number of highest test scores of each user with which
     * compute the MSI value
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

        Map<String, Map<String, Score>> trainingScores = scores.getTrainingScoreMap();
        Map<String, Map<String, Score>> predictedScores = scores.getPredictedTestScoreMap();

        Map<String, Integer> nUsersWithItem = new HashMap<String, Integer>();
        for (String user : trainingScores.keySet()) {
            Map<String, Score> userItems = trainingScores.get(user);
            for (String item : userItems.keySet()) {
                if (userItems.get(item).getValue() >= this.relevanceScoreThreshold) {
                    if (!nUsersWithItem.containsKey(item)) {
                        nUsersWithItem.put(item, 0);
                    }
                    nUsersWithItem.put(item, 1 + nUsersWithItem.get(item));
                }
            }
        }

        double msi = 0.0;

        int nUsers = trainingScores.size();

        for (String user : trainingScores.keySet()) {

            if (!predictedScores.containsKey(user)) {
                nUsers--;
                continue;
            }

            ArrayList<Score> userPredictedScores = new ArrayList<Score>(predictedScores.get(user).values());
            Collections.sort(userPredictedScores);

            if (userPredictedScores.isEmpty()) {
                nUsers--;
                continue;
            }

            double userMSI = 0.0;
            int n = 0;
            for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                int nUsersWithI = nUsersWithItem.get(userPredictedScores.get(i).getItemId()) == null ? 0 : nUsersWithItem.get(userPredictedScores.get(i).getItemId());
                userMSI += nUsersWithI == 0 ? 0 : Math.log(nUsersWithI);
                n++;
            }
            userMSI /= n;

            msi += userMSI;
        }

        msi /= nUsers;

        return msi;
    }
}
