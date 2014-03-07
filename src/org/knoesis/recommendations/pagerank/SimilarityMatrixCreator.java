package org.knoesis.recommendations.pagerank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.knoesis.utils.Serializer;
import org.knoesis.utils.ProjectVariables;



/**
 * This class is used to preprocess the file contains the similarity
 * and create the matrix with similarities between 
 * 
 * @author Sarasi
 *
 */


public class SimilarityMatrixCreator {

	/**
	 * @Main method will create the probability matrix and serialized it
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimilarityMatrixCreator aMatCon = new SimilarityMatrixCreator();
		/*aMatCon.readFilesForDataStructure(ProjectVariables.strdataFolder+File.separator
				+ProjectVariables.strInputSimilarityFolder+File.separator+ProjectVariables.inputSimilarityFile);
		aMatCon.createProbabilityMatrix();*/
		aMatCon.createRowStochasticMatrix();
		
	}
	
	
	/** 
	 * This method will read the input file with similarities and create and serialize the data
	 * structures needed to create similarity matrix
	 * 
	 * @param Input file with similarity scores of entities
	 * @return 
	 */
	
	public void readFilesForDataStructure(String iFileName){
	
		try {
			Set<String> aCompleteSet = new LinkedHashSet<String>();//All the entities
			//List bookCol1, bookCol2, entityScore will store the values of each line
			List<String> bookCol1 = new LinkedList<String>();
			List<String> bookCol2 = new LinkedList<String>();
			List<Double> entityScore = new LinkedList<Double>();
			
			Scanner aScan = new Scanner(new File(iFileName));
			while(aScan.hasNextLine()){
				String aLine = aScan.nextLine().trim();
				String[] items = aLine.split("\t");
				if(items.length == 3){
					bookCol1.add(items[0].trim());
					bookCol2.add(items[1].trim());
					aCompleteSet.add(items[0].trim());
					aCompleteSet.add(items[1].trim());
					double dScore = Double.parseDouble(items[2].trim());
					entityScore.add(dScore);
				}
			}
			
			aScan.close();
			//Convert the Set of aCompleteSet to a List for using the indexing in the list as our index
			List<String> aEntityIndexedList = new LinkedList<String>(aCompleteSet);
			
			//Serilazie the object files
			Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
					ProjectVariables.strSerialzedDataFolder+File.separator+
					ProjectVariables.serElementListFile, aEntityIndexedList);
			Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
					ProjectVariables.strSerialzedDataFolder+File.separator+
					ProjectVariables.serCol1File, bookCol1);
			Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
					ProjectVariables.strSerialzedDataFolder+File.separator+
					ProjectVariables.serCol2File, bookCol2);
			Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
					ProjectVariables.strSerialzedDataFolder+File.separator+
					ProjectVariables.serScoreFile, entityScore);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/** 
	 * This method will use the data structured created after scanning the files to 
	 * Create the probability Matrix and serialiazed the multidimensional array
	 * 
	 * @param 
	 * @return 
	 */
	
	public void createProbabilityMatrix(){
		
		//Load the serialized objects
		List<String> listOfElements = (List<String>) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serElementListFile);
		List<String> bookCol1 = (List<String>) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serCol1File);
		List<String> bookCol2 = (List<String>) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serCol2File);
		List<Double> Score = (List<Double>) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serScoreFile);
		
		//Unique number of elements
		int iEntityLength = listOfElements.size();
		//Number of Similarities
		int iEntries = bookCol1.size();
		
		//Create the probability matrix to store the values
		double[][] similarityMatrix = new double[iEntityLength][iEntityLength];
		
		for(int i = 0; i < iEntries; i++){
			//Get each similarity pair
			String aBook1 = bookCol1.get(i);
			String aBook2 = bookCol2.get(i);
			Double aScore = Score.get(i);
			
			//Get the indexing of each element in the list and store it in 2-dimensional array
			int iPosBook1 = listOfElements.indexOf(aBook1);
			int iPosBook2 = listOfElements.indexOf(aBook2);
			similarityMatrix[iPosBook1][iPosBook2] = aScore;
			similarityMatrix[iPosBook2][iPosBook1] = aScore;
		}
	
		//Serialize the Matrix
		Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serMatrix, similarityMatrix);
		
		
	}
	
	
	/**
	 * Given a matrix with a similarities, this method will convert
	 * the matrix in to a row stochastic matrix (probabilities at each row will be 1)
	 * 
	 * @param Matrix with similarities of books
	 * @return Row stochastic matrix
	 */
	
	public void createRowStochasticMatrix(){
		
		double[][] stochasticSimilarityMatrix = (double[][]) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serMatrix);
		
		int iLength = stochasticSimilarityMatrix.length;
		for(int i = 0; i < iLength; i++){
			
			//calculate the row sum
			double dRowSum = 0.0;
			for(int j = 0; j < iLength; j++){
				dRowSum = dRowSum + stochasticSimilarityMatrix[i][j];
			}
			
			//divided by row sum to make each row's probability one except row sum equals to 0
			if(dRowSum != 0.0){
				for(int j =0 ; j < iLength; j++){
					stochasticSimilarityMatrix[i][j] = stochasticSimilarityMatrix[i][j]/dRowSum;
				}
			}
			
		}
		
		Serializer.serialize(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serStochasticMatrix, stochasticSimilarityMatrix);
		
	}
	
	

}
