/**
 * SRecall.java
 *
 * Computation of the S-recall diversity value on a set of predicted test scores
 * (ratings) from a recommendation method.
 *
 * For details about the S-recall formula, see:
 *
 * Zhai, C. X., Cohen, W. W., Lafferty, J. 2003. Beyond Independent Relevance:
 * Methods and Evaluation Metrics for Subtopic Retrieval. In Proceedings of the
 * 26th Annual International ACM SIGIR Conference on Research and Development in
 * Informaion Retrieval (SIGIR'03), 10-17.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics.diversity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;
import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationItemMetadata;
import org.eswc2014.challenge.lodrecsys.evaluation.Score;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;

public class SRecall implements Metric {

    public static final int ITEM_ATTRIBUTES = 1;
    public static final int ITEM_CATEGORIES = 2;
    private EvaluationItemMetadata itemMetadata;
    private int itemMetadataType;

    /**
     * Builds a SRecall instance using the given item metadata (content
     * attributes and categories), and considering a particular item metadata
     * type.
     *
     * @param itemMetadata an object that stores the items' metadata
     * @param itemMetadataType an integer value indicating which type of item
     * metadata (SRecall.ITEM_ATTRIBUTES or SRecall.ITEM_CATEGORIES) has to be
     * used for the S-recall computation
     */
    public SRecall(EvaluationItemMetadata itemMetadata, int itemMetadataType) throws IllegalArgumentException {
        if (itemMetadata == null) {
            throw new IllegalArgumentException("null data manager");
        }
        if (itemMetadataType != ITEM_ATTRIBUTES && itemMetadataType != ITEM_CATEGORIES) {
            throw new IllegalArgumentException("invalid item topic type");
        }

        this.itemMetadata = itemMetadata;
        this.itemMetadataType = itemMetadataType;
    }

    /**
     * Computes the S-recall value on the predicted test scores given as input
     * arguments.
     *
     * @param scores test scores with which compute the S-recall value
     *
     * @throws Exception in case of null argument
     */
    @Override
    public double evaluate(EvaluationScores scores) throws Exception {
        return this.evaluate(scores, Integer.MAX_VALUE);
    }

    /**
     * Computes the S-recall value on the predicted test scores given as input
     * arguments, only considering the k highest test scores of each user.
     *
     * @param scores test scores with which compute the S-recall value
     * @param k the number of highest test scores of each user with which
     * compute the S-recall value
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

        double srecall = 0.0;

        int nUsers = realScores.size();

        for (String user : realScores.keySet()) {

            ArrayList<Score> userPredictedScores = new ArrayList<Score>(predictedScores.get(user).values());
            Collections.sort(userPredictedScores);

            if (userPredictedScores.isEmpty()) {
                nUsers--;
                continue;
            }

            switch (this.itemMetadataType) {
                case ITEM_ATTRIBUTES:
                    List<String> userAttributes = new ArrayList<String>();
                    for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                        Set<String> itemAttributes = this.itemMetadata.getItem(userPredictedScores.get(i).getItemId()).getAttributes().keySet();
                        for (String a : itemAttributes) {
                            if (!userAttributes.contains(a)) {
                                userAttributes.add(a);
                            }
                        }
                    }
                    srecall += userAttributes.size() / this.itemMetadata.getNumAttributes();
                    break;
                case ITEM_CATEGORIES:
                    List<String> userCategories = new ArrayList<String>();
                    for (int i = 0; i < userPredictedScores.size() && i < k; i++) {
                        Set<String> itemCategories = this.itemMetadata.getItem(userPredictedScores.get(i).getItemId()).getCategories().keySet();
                        for (String c : itemCategories) {
                            if (!userCategories.contains(c)) {
                                userCategories.add(c);
                            }
                        }
                    }
                    srecall += userCategories.size() / this.itemMetadata.getNumCategories();
                    break;
            }
        }

        srecall /= nUsers;

        return srecall;
    }
}