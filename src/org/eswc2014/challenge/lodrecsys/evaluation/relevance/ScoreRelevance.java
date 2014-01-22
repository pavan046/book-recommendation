/**
 * ScoreRelevance.java
 *
 * Interface to be satisfied by any class that implements a method to establish
 * the relevance value of a score assigned to a user-item pair.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.relevance;

public interface ScoreRelevance {

    /**
     * Returns the relevance value of a given score assigned to a user-item
     * pair.
     *
     * @param score score whose relevance value is returned
     * @return the relevance value of the input score
     */
    public double relevanceOf(double score);
}
