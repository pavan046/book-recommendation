/**
 * ILS.java
 *
 * Computation of the Intra-List Similarity (ILS) diversity value on a set of
 * predicted test scores (ratings) from a recommendation method.
 *
 * For details about the ILS formula, see:
 *
 * Ziegler, C.-N., McNee, S. M., Konstan, J. A., Lausen, G. 2005. Improving
 * Recommendation Lists through Topic Diversification. In Proceedings of the
 * 14th International Conference on World Wide Web (WWW'05), pp. 22-32.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.diversity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationItemMetadata;
import org.eswc2014.challenge.lodrecsys.evaluation.Item;
import org.eswc2014.challenge.lodrecsys.evaluation.similarity.ItemSimilarity;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;

public class ILS implements Metric {

    private EvaluationItemMetadata itemMetadata;
    private ItemSimilarity itemSimilarity;

    /**
     * Builds an ILS instance using the given item metadata (content attributes
     * and categories) and item similarity function.
     *
     * @param itemMetadata an object that stores the items' metadata
     * @param itemSimilarity an object that computes the (content-based)
     * similarity between two items
     */
    public ILS(EvaluationItemMetadata itemMetadata, ItemSimilarity itemSimilarity) throws IllegalArgumentException {
        if (itemMetadata == null) {
            throw new IllegalArgumentException("null item metadata");
        }
        if (itemSimilarity == null) {
            throw new IllegalArgumentException("null item similarity");
        }

        this.itemMetadata = itemMetadata;
        this.itemSimilarity = itemSimilarity;
    }

    /**
     * Computes the ILS value on the predicted test scores given as input
     * arguments.
     *
     * @param scores test scores with which compute the ILS value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        return this.evaluate(scores, Integer.MAX_VALUE);
    }

    /**
     * Computes the ILS value on the predicted test scores given as input
     * arguments, only considering the k highest test scores of each user.
     *
     * @param scores test scores with which compute the ILS value
     * @param k the number of highest test scores of each user with which
     * compute the ILS value
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

        double ils = 0.0;

        int nUsers = realScores.size();

        for (String user : realScores.keySet()) {

            ArrayList<Score> userPredictedScores = new ArrayList<Score>(predictedScores.get(user).values());
            Collections.sort(userPredictedScores);

            if (userPredictedScores.isEmpty()) {
                nUsers--;
                continue;
            }

            double userILS = 0.0;
            int n = 0;
            for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                Item item1 = this.itemMetadata.getItem(userPredictedScores.get(i).getItemId());
                for (int j = 0; j < userPredictedScores.size() && j < k; j++) {
                    Item item2 = this.itemMetadata.getItem(userPredictedScores.get(j).getItemId());
                    userILS += (1.0 - this.itemSimilarity.similarityOf(item1, item2));
                    n++;
                }
            }
            userILS /= 2 * n;
            ils += userILS;
        }

        ils /= nUsers;

        return ils;
    }
}
