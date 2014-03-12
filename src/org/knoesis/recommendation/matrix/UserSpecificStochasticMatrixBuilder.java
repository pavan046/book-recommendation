package org.knoesis.recommendation.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.knoesis.userprofile.UserProfileGeneratorFromESWCData;
import org.knoesis.utils.ProjectVariables;
import org.mortbay.jetty.servlet.HashSessionIdManager;

public class UserSpecificStochasticMatrixBuilder implements MatrixBuilder{
	
	private static String userProfileFile, matrixFileName; 
	private static Set<String> userProfileEntities; 
	/**
	 * Needs the user 
	 * @param userProfileFile
	 * @param matrixFileName
	 */
	public UserSpecificStochasticMatrixBuilder(String userProfileFile, String matrixFileName) {
		this.userProfileFile = ProjectVariables.strdataFolder+File.separator
				+ProjectVariables.strUserFolder+File.separator+2;; 
		readUserProfile(); 
		//For now 
    	this.matrixFileName = ProjectVariables.strdataFolder+File.separator
		+ProjectVariables.strInputSimilarityFolder+File.separator+ProjectVariables.inputSimilarityFile;
	}
	/**
	 * 
	 */
	private void readUserProfile() {
		BufferedReader read;
		String line=null; 
		try {
			read = new BufferedReader(new FileReader(this.userProfileFile));
			while((line = read.readLine())!=null){
				String[] objects = line.split("\t"); 
				userProfileEntities.add(objects[0]); 
			}
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, HashMap<String, Double>> mapMatrixBuilder() {
		String strFileName = this.matrixFileName;
        BufferedReader read;
    	Map<String, HashMap<String, Double>> matrix = new HashMap<String, HashMap<String,Double>>();
	     
		try {
			read = new BufferedReader(new FileReader(strFileName));
		   String line = null;
	        int i=0;
	        
	        while((line = read.readLine())!=null){
	            i++;
	            String[] objects = line.split("\t");
	            HashMap<String, Double> relatedEntitiesScore;
	            //HashMap<String, Double> relatedEntitiesScore2 = new HashMap<>();
	            // For A
	            if(matrix.keySet().contains(objects[0].trim())){
	                relatedEntitiesScore = matrix.get(objects[0].trim());
	                relatedEntitiesScore.put(objects[1].trim(), Double.parseDouble(objects[2].trim()));
	                Double sum = relatedEntitiesScore.get("sum") + Double.parseDouble(objects[2].trim());
	                relatedEntitiesScore.put("sum", sum);    
	                matrix.put(objects[0].trim(), relatedEntitiesScore); 
	            } else{
	                relatedEntitiesScore = new HashMap<>();
	                relatedEntitiesScore.put(objects[1].trim(), Double.parseDouble(objects[2].trim()));
	                relatedEntitiesScore.put("sum", Double.parseDouble(objects[2].trim()));
	                matrix.put(objects[0].trim(), relatedEntitiesScore); 
	            }
	            // For B
	            //relatedEntitiesScore = new HashMap<>();
	            if(matrix.keySet().contains(objects[1].trim())){
	                relatedEntitiesScore = matrix.get(objects[1].trim());
	                relatedEntitiesScore.put(objects[0].trim(), Double.parseDouble(objects[2].trim()));
	                Double sum = relatedEntitiesScore.get("sum") + Double.parseDouble(objects[2].trim());
	                relatedEntitiesScore.put("sum", sum);    
	                matrix.put(objects[1].trim(), relatedEntitiesScore); 
	            } else{
	                relatedEntitiesScore = new HashMap<>();
	                relatedEntitiesScore.put(objects[0].trim(), Double.parseDouble(objects[2].trim()));
	                relatedEntitiesScore.put("sum", Double.parseDouble(objects[2].trim()));
	                matrix.put(objects[1].trim(), relatedEntitiesScore); 
	            }
	            
	            if(i%100000 == 0)
	                System.out.println(i+":"+System.currentTimeMillis());
	        }
	        read.close();
	        System.out.println("Total Number of Books: "+matrix.keySet().size());
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		removeUserNonSpecificNodes(matrix); 
        return matrix;
   }
	/**
	 * Remove entities that are not at hop 2 from the profiled entities
	 * @param matrix
	 */
	private void removeUserNonSpecificNodes(
			Map<String, HashMap<String, Double>> matrix) {
		Set<String> userEntities = new HashSet<>(); 
		userEntities.addAll(userProfileEntities); 
		for(String entity: userProfileEntities){
			userEntities.addAll(matrix.get(entity).keySet());
		}
		for(String entity:matrix.keySet()){
			if(!userEntities.contains(entity))
				matrix.remove(entity);
		}
	}

}
