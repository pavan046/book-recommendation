/**
 * Metric.java
 *
 * Interface to be satisfied by any performance (precision, ranking, diversity
 * novelty) metric that is computed on sets of training and test scores
 * (ratings) from a recommendation method.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation.metrics;

import org.eswc2014.challenge.lodrecsys.evaluation.EvaluationScores;

public interface Metric {

    /**
     * Computes the metric value on the training and test score sets given as
     * input arguments.
     *
     * @param scores training and test scores with which compute the metric
     * value
     *
     * @throws Exception in case of null or wrong wrong argument(s)
     */
    public abstract double evaluate(EvaluationScores scores) throws Exception;

    /**
     * Computes the metric value on the training and test score sets given as
     * input arguments, only considering the k highest test scores of each user.
     *
     * @param scores training and test scores with which compute the metric
     * value
     * @param k the number of highest test scores of each user with which
     * compute the metric value
     *
     * @throws Exception in case of null or wrong wrong argument(s)
     */
    public abstract double evaluate(EvaluationScores scores, int k) throws Exception;
}
