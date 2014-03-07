package org.knoesis.recommendations.pagerank;

import java.io.File;

import org.knoesis.utils.ProjectVariables;
import org.knoesis.utils.Serializer;

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
		/*EntityRankCalculator aRankCal = new EntityRankCalculator();
		double[][] similarityBookMatrix = {{0.0,0.7273,0.0909,0.1818},
										 	{0.5161,0.0,0.3226,0.1613},
										 	{0.0667,0.3333,0.0,0.6},
										 	{0.1481,0.1852,0.6667,0.0}};
		double[] initRankVector = {1, 1, 1, 1};
		double[] userRating = {0.5, 0.2, 0.1, 0.2};
		aRankCal.calculateEntityRank(similarityBookMatrix, initRankVector, userRating);*/
		
		/*double[][] simBook = {{0.3, 0.2, 0.0},{0.5, 0.1, 0.0},{0.0, 0.0, 0.0}};
		aRankCal.createRowStochasticMatrix(simBook);*/
		
		EntityRankCalculator aRankCal = new EntityRankCalculator();
		aRankCal.entityRankingPerUser();
		
	}
	
	/**
	 * This method will iterate over each users rating profile and create a ranked list of 
	 * book ratings and print the output which is a ranked list of books for each user
	 * 
	 * @param
	 * @return 
	 */

	public void entityRankingPerUser(){
		
		//Load the row stochastic matrix
		double[][] rowStochasticSimilarityMatrix = (double[][]) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serStochasticMatrix);
		int iLength = rowStochasticSimilarityMatrix.length;
		//create the initial vector with value one
		double[] initRankVector = new double[iLength];
		ResultHandler aResultHandler = new ResultHandler();
		for(int i = 0; i < iLength; i++){
			initRankVector[i] = 1.00;
		}
		
		//Iterate over every user profile and calculate the rank
		File folder = new File(ProjectVariables.strdataFolder+File.separator
				+ProjectVariables.strUserFolder);
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) {
			 if (listOfFiles[i].isFile()) {
				 String strFileName = listOfFiles[i].getName();
				 UserRatingCreator aUserRator = new UserRatingCreator();
				 double[] userRatingVector = aUserRator.userProfileGenerator(listOfFiles[i]);
				 double[] personalizedBookRank = calculateEntityRank(rowStochasticSimilarityMatrix,
						 initRankVector, userRatingVector);
				 aResultHandler.resultRanking(personalizedBookRank, strFileName);
				 
				 
			 }
		 }		
	}
	
	
	/**
	 * . 
	 * This method will calculate the ranking of each
	 * entity after a given number of iteration based on the user's profile
	 * 
	 * @param Row stochastic Matrix of entities with similarity measures between each entity
	 * @param Initial Rank of all the entities
	 * @param Normalized user ratings
	 * @return Ranked Entities
	 */
	
	public double[] calculateEntityRank(double[][] rowStochasticSimilarityMatrix, 
			double[] entityRankVector, double[] userRating){
		
		int iIteCount = 0;
		int iEntityCount = entityRankVector.length;
	
		while(iIteCount < ProjectVariables.MAX_ITERATIONS){ //LOOP will iterate over MAX_ITERATIONS
			
			for(int iElement = 0; iElement < iEntityCount; iElement++){
				
				double dWeightedSum = 0.0;
				double dUserRating = userRating[iElement];
				
				for (int j = 0; j < iEntityCount; j++){ // Get all the connected edges for the given entity 
					
					if(rowStochasticSimilarityMatrix[iElement][j]!= 0){ // calculate the sum of contributing value from each connected edge
						dWeightedSum = dWeightedSum + entityRankVector[j] * 
								rowStochasticSimilarityMatrix[j][iElement];
						
					}
				}
				
				double dEntityNewRank = (1 - ProjectVariables.DAMPING_FACTOR) * dUserRating + //personalized page rank
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
