/**
 * ItemSimilarity.java
 *
 * Interface to be satisfied by any (content-based) item similarity.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.similarity;

import org.eswc2014.challenge.lodrecsys.evaluation.Item;

public interface ItemSimilarity {

    /**
     * Computes the similarity value between two input items.
     *
     * @param item1 first item with which compute the similarity value
     * @param item2 second item with which compute the similarity value
     * @return the similarity value between item1 and item2
     *
     * @throws Exception in case of error
     */
    public double similarityOf(Item item1, Item item2) throws Exception;
}
