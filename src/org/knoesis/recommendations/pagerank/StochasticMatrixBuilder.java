package org.knoesis.recommendations.pagerank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.knoesis.utils.ProjectVariables;

public class StochasticMatrixBuilder {
	
	
    public static void main(String[] args) throws IOException {
    	
    	StochasticMatrixBuilder aMapMatrixBuilder = new StochasticMatrixBuilder();
    	aMapMatrixBuilder.mapMatrixBuilder();
    }
    
    public void mapMatrixBuilder() {
    	
    	String strFileName = ProjectVariables.strdataFolder+File.separator
				+ProjectVariables.strInputSimilarityFolder+File.separator+ProjectVariables.inputSimilarityFile;
        BufferedReader read;
		try {
			read = new BufferedReader(new FileReader(strFileName));
			Map<String, HashMap<String, Double>> matrix = new HashMap<String, HashMap<String,Double>>();
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
	        i=0; 
	        /*for(String book: matrix.keySet()){
	            i++;
	            System.out.println(i+": "+ book+": " + matrix.get(book).size() + " :"+matrix.get(book).get("sum"));
	        }*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}