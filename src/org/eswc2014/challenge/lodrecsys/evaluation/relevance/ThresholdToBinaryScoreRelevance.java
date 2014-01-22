/**
 * ThresholdToBinaryScoreRelevance.java
 *
 * Converts a double score value into 1 if the score is assigned to a relevant
 * item for a user, and 0 otherwise. An item is considered as relevant for a
 * user if its score value is equal or greater than certain threshold value.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.relevance;

public class ThresholdToBinaryScoreRelevance implements ScoreRelevance {

    private double thresholdValue;

    /**
     * Builds a ThresholdToBinaryScoreRelevance instance with the given
     * relevance threshold value.
     */
    public ThresholdToBinaryScoreRelevance(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    @Override
    public double relevanceOf(double score) {
        return score >= this.thresholdValue ? 1 : 0;
    }
}
