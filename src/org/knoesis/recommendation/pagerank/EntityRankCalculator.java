package org.knoesis.recommendation.pagerank;

import org.knoesis.utils.ProjectVariables;

/**
 * This class is used to calculate the rankings of the entity based
 * on the similarity measures calculated between the entities. This
 * assumes entity connection graph is bi directional
 * 
 * @author Sarasi
 *
 */

public class EntityRankCalculator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] similarityBookMatrix = {{0.0,0.7273,0.0909,0.1818},
										 	{0.5161,0.0,0.3226,0.1613},
										 	{0.0667,0.3333,0.0,0.6},
										 	{0.1481,0.1852,0.6667,0.0}};
		double[] initRankVector = {1, 1, 1,1};
		EntityRankCalculator aRankCal = new EntityRankCalculator();
		aRankCal.calculateEntityRank(similarityBookMatrix, initRankVector);
	}
	
	
	/**
	 * This is the main function to call for this class. 
	 * This method will calculate the ranking of each
	 * entity after a given number of iteration
	 * 
	 * @param Row stochastic Matrix of entities with similarity measures between each entity
	 * @param Initial Rank of all the entities
	 * @return Ranked Entities
	 */
	
	public double[] calculateEntityRank(double[][] rowStochasticSimilarityMatrix, 
			double[] entityRankVector){
		
		int iIteCount = 0;
		int iEntityCount = entityRankVector.length;
		
		
		while(iIteCount < ProjectVariables.MAX_ITERATIONS){ //LOOP will iterate over MAX_ITERATIONS
		
			
			for(int iElement = 0; iElement < iEntityCount; iElement++){
				
				double dWeightedSum = 0.0;
				for (int j = 0; j < iEntityCount; j++){ // Get all the connected edges for the given entity 
					
					if(rowStochasticSimilarityMatrix[iElement][j]!= 0){ // calculate the sum of contributing value from each connected edge
						dWeightedSum = dWeightedSum + entityRankVector[j] * // Here it will take the old value even though it might be updated
								rowStochasticSimilarityMatrix[j][iElement];
						
					}
				}
				
				double dEntityNewRank = (1 - ProjectVariables.DAMPING_FACTOR)/iEntityCount + //calculate the new page rank
						ProjectVariables.DAMPING_FACTOR * dWeightedSum;	
				entityRankVector[iElement] = dEntityNewRank;
				
			}
			iIteCount++;
		}
		
		for(double dRank : entityRankVector){
			System.out.println(dRank);
		}
		return entityRankVector;
		
	}
	
}
