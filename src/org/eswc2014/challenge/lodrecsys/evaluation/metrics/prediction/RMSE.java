/**
 * RMSE.java
 *
 * Computation of the Root Mean Squared Error (RMSE) value on a set of real and
 * predicted test scores (ratings) from a recommendation method.
 *
 * For details about the RMSE formula, see:
 *
 * Ricci, F., Rokach, L., Shapira, B., Kantor, P. B. 2011. Recommender Systems
 * Handbook. ISBN 978-0-387-85819-7.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction;

import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;

public class RMSE implements Metric {

    /**
     * Builds an empty RMSE instance.
     */
    public RMSE() {
    }

    /**
     * Computes the MAE value on the real and predicted test scores given as
     * input arguments.
     *
     * @param scores test scores with which compute the RMSE value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        if (scores == null) {
            throw new IllegalArgumentException("null score set");
        }

        Map<String, Map<String, Score>> realScores = scores.getRealTestScoreMap();
        Map<String, Map<String, Score>> predictedScores = scores.getPredictedTestScoreMap();

        double rmse = 0.0;

        int n = 0;
        for (String user : realScores.keySet()) {
            for (String item : realScores.get(user).keySet()) {
                double realScore = realScores.get(user).get(item).getValue();
                double predictedScore = predictedScores.get(user).get(item).getValue();

                rmse += (predictedScore - realScore) * (predictedScore - realScore);
                n++;
            }
        }
        rmse /= n;
        rmse = Math.sqrt(rmse);

        return rmse;
    }

    /**
     * Unsupported method: a RMSE value has to be computed for all test scores.
     *
     * @throws UnsupportedOperationException in all cases
     */
    @Override
    public double evaluate(EvaluationScores scores, int k) throws Exception {
        throw new UnsupportedOperationException("RMSE has to be computed for all test scores");
    }
}
