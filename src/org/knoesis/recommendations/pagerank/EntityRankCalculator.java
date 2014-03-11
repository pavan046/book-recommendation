package org.knoesis.recommendations.pagerank;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		EntityRankCalculator aRankCal = new EntityRankCalculator();
		aRankCal.entityRankingUserMap();
		
	}
	
	/**
	 * Main conrtroller method will iterate over each users rating profile and create a ranked list of 
	 */
	 
	public void entityRankingUserMap(){
		
		StochasticMatrixBuilder aStochasticMatrixBuilder = new StochasticMatrixBuilder();
		Map<String, HashMap<String, Double>> matrix = aStochasticMatrixBuilder.mapMatrixBuilder();
		
		ResultHandler aResultHandler = new ResultHandler();
		//Create the initial map
		Map<String, Double> mapEntityRank = new HashMap<String, Double>();
		for(String aElement: matrix.keySet()){
			mapEntityRank.put(aElement, 0.00);
		}
		
		//User profiles
		//Iterate over every user profile and calculate the rank
		File folder = new File(ProjectVariables.strdataFolder+File.separator
						+ProjectVariables.strUserFolder);
		File[] listOfFiles = folder.listFiles(); 
		
		for (int i = 0; i < listOfFiles.length; i++) {//each file contains a user profile
			 if (listOfFiles[i].isFile()) {
				 String strFileName = listOfFiles[i].getName();
				 //Get the relevant User profile
				 UserRatingCreator aUserRator = new UserRatingCreator();
				 Map<String, Double> mapOfUserProfile = aUserRator.userProfileGenerator(listOfFiles[i]);
				 System.out.println("Begin page rank"+System.currentTimeMillis());
				 Map<String, Double> newPersonalizedRank = calculateEntityRankMap(matrix, mapEntityRank, mapOfUserProfile);
				 System.out.println("End page rank"+System.currentTimeMillis());
				 aResultHandler.resultRanking(newPersonalizedRank, strFileName);
				 
			 }
		}
		
		
	}
	
	/**
	 * This method will iterate over each users rating profile and create a ranked list of 
	 * book ratings and print the output which is a ranked list of books for each user
	 * 
	 * @param
	 * @return 
	 */

	/*public void entityRankingPerUser(){
		
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
				// aResultHandler.resultRanking(personalizedBookRank, strFileName);
				 
				 
			 }
		 }		
	}*/
	
	/**
	 * Map based page rank algorithm
	 * @param mapOfSimilarity
	 * @param mapEntityRank
	 * @param mapUserRating
	 * @return
	 */
	public Map<String, Double> calculateEntityRankMap(Map<String, HashMap<String, Double>> mapOfSimilarity,
			Map<String, Double> mapEntityRank, Map<String, Double> mapUserRating){
		
		int iIteCount = 0;
		
		while(iIteCount < ProjectVariables.MAX_ITERATIONS){ //LOOP will iterate over MAX_ITERATIONS
			
			Set<String> aSetofEntities = mapEntityRank.keySet();
			for(String aEntity : aSetofEntities){
				
				double dWeightedSum = 0.0;
				double dUserRating = 0.0;
				double dCurrentRankValue = mapEntityRank.get(aEntity);
				
				if(mapUserRating.containsKey(aEntity)){
					dUserRating = mapUserRating.get(aEntity);
				}
				
				//Get the connected edges and calculate the weighted sum 
				Map<String, Double> connectedEdges = mapOfSimilarity.get(aEntity);
				Set<String> connectedEntities = connectedEdges.keySet();
				
				for(String aConnection : connectedEntities){
					if(!aConnection.equalsIgnoreCase("sum")){
						Map<String, Double> aMapOfConnectedEdges = mapOfSimilarity.get(aConnection);
						double dValue = aMapOfConnectedEdges.get(aEntity);
						double dSum = aMapOfConnectedEdges.get("sum");
						dWeightedSum = dWeightedSum + dCurrentRankValue*(dValue/dSum);
					}
				}
				
				//Calculate the new rank value
				double dEntityNewRank = (1 - ProjectVariables.DAMPING_FACTOR) * dUserRating + //personalized page rank
						ProjectVariables.DAMPING_FACTOR * dWeightedSum;	
				mapEntityRank.put(aEntity, dEntityNewRank);
				
			}
			
			System.out.println("Iteration :"+iIteCount);
			iIteCount++;
			
		}
		
		return mapEntityRank;
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
