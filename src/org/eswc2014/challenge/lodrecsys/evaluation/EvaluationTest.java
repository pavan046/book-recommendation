package org.eswc2014.challenge.lodrecsys.evaluation;

import org.eswc2014.challenge.lodrecsys.evaluation.similarity.ItemAttributeCosineSimilarity;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.Metric;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.diversity.SRecall;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.diversity.ILS;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.novelty.EBN;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.novelty.MSI;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction.MAE;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction.Precision;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction.RMSE;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.prediction.Recall;
import org.eswc2014.challenge.lodrecsys.evaluation.metrics.ranking.NDCG;
import org.eswc2014.challenge.lodrecsys.evaluation.relevance.ThresholdToBinaryScoreRelevance;
import org.eswc2014.challenge.lodrecsys.evaluation.similarity.ItemCategoryCosineSimilarity;

public class EvaluationTest {

    public static void main(String[] args) {
        try {
            String scoreTrainingFile = "scores-training.dat";
            String scoreTestFile = "scores-test.dat";

            String itemAttributeFile = "item-attributes.dat";
            String itemCategoryFile = "item-categories.dat";

            EvaluationScores scores = new EvaluationScores(scoreTrainingFile, scoreTestFile);

            Metric mae = new MAE();
            System.out.println("MAE:\t" + mae.evaluate(scores));

            Metric rmse = new RMSE();
            System.out.println("RMSE:\t" + rmse.evaluate(scores));

            double relevanceScoreThreshold = 5;
            int N = 5;

            Metric precision = new Precision(relevanceScoreThreshold);
            System.out.print("P@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + precision.evaluate(scores, k));
            }
            System.out.println("");

            Metric recall = new Recall(relevanceScoreThreshold);
            System.out.print("R@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + recall.evaluate(scores, k));
            }
            System.out.println("");

            Metric ndcg = new NDCG(new ThresholdToBinaryScoreRelevance(relevanceScoreThreshold));
            System.out.print("nDCG@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + ndcg.evaluate(scores, k));
            }
            System.out.println("");

            EvaluationItemMetadata itemMetadataManager = new EvaluationItemMetadata(itemAttributeFile, itemCategoryFile);

            Metric ils = new ILS(itemMetadataManager, new ItemAttributeCosineSimilarity());
            System.out.print("ILS-att@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + ils.evaluate(scores, k));
            }
            System.out.println("");

            ils = new ILS(itemMetadataManager, new ItemCategoryCosineSimilarity());
            System.out.print("ILS-cat@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + ils.evaluate(scores, k));
            }
            System.out.println("");

            Metric sRecall = new SRecall(itemMetadataManager, SRecall.ITEM_ATTRIBUTES);
            System.out.print("s-recall-att@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + sRecall.evaluate(scores, k));
            }
            System.out.println("");

            sRecall = new SRecall(itemMetadataManager, SRecall.ITEM_CATEGORIES);
            System.out.print("s-recall-cat@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + sRecall.evaluate(scores, k));
            }
            System.out.println("");

            Metric msi = new MSI(relevanceScoreThreshold);
            System.out.print("MSI@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + msi.evaluate(scores, k));
            }
            System.out.println("");

            Metric ebn = new EBN(relevanceScoreThreshold);
            System.out.print("EBN@k:");
            for (int k = 1; k <= N; k++) {
                System.out.print("\t" + ebn.evaluate(scores, k));
            }
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
