/**
 * ItemAttributeSimilarity.java
 *
 * Abstract class to be extended by any content attribute-based item similarity.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.similarity;

import org.eswc2014.challenge.lodrecsys.evaluation.Item;

public abstract class ItemAttributeSimilarity implements ItemSimilarity {

    @Override
    public abstract double similarityOf(Item item1, Item item2) throws Exception;
}
