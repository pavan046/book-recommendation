package org.knoesis.recommendation.matrix;

import java.util.HashMap;
import java.util.Map;

public interface MatrixBuilder {
	/**
	 * Builds the matrix and returns a map with node, weighted adjacency nodes
	 * @return
	 */
	public Map<String, HashMap<String, Double>> mapMatrixBuilder();
}
