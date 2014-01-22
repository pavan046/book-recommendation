/**
 * ItemCategoryCosineSimilarity.java
 *
 * Computation of the cosine similarity of two items represented as vectors of
 * weighted categories.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.similarity;

import java.util.Map;
import org.eswc2014.challenge.lodrecsys.evaluation.Item;

public class ItemCategoryCosineSimilarity extends ItemCategorySimilarity {

    /**
     * Builds an empty ItemCategoryCosineSimilarity instance.
     */
    public ItemCategoryCosineSimilarity() {
    }

    @Override
    public double similarityOf(Item item1, Item item2) throws Exception {
        if (item1 == null || item2 == null) {
            throw new IllegalArgumentException("null item");
        }

        Map<String, Double> vector1 = item1.getCategories();
        Map<String, Double> vector2 = item2.getCategories();

        double module1 = 0.0;
        for (Double x : vector1.values()) {
            module1 += x * x;
        }
        module1 = Math.sqrt(module1);

        double module2 = 0.0;
        for (Double x : vector2.values()) {
            module2 += x * x;
        }
        module2 = Math.sqrt(module2);

        double sim = 0.0;
        if (module1 != 0 && module2 != 0) {
            for (String a : vector1.keySet()) {
                double x1 = vector1.get(a);
                if (vector2.containsKey(a)) {
                    double x2 = vector2.get(a);
                    sim += x1 * x2;
                }
            }
            sim /= module1 * module2;
        }
        return sim;
    }
}
