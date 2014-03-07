package org.knoesis.recommendations.pagerank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knoesis.utils.ProjectVariables;
import org.knoesis.utils.Serializer;

/**
 * This class is used to rank and print the result for each user
 *
 * @author Sarasi
 */

public class ResultHandler {
	
	/**
	 * 
	 * This method will rank the results and print it into a file
	 * 
	 * @param map 
	 * @return Sorted map based on the value 
	 */
	
	public void resultRanking(double[] scoreList, String fileName){
		
		List<String> listOfElements = (List<String>) Serializer.load(ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strSerialzedDataFolder+File.separator+
				ProjectVariables.serElementListFile);
		
		Map<String, Double> aMapOfBookRankings = new HashMap<String, Double>();
		
		//Store the book values in a map with URI as the key and score as the value
		for(int i = 0; i < listOfElements.size(); i++){
			String strBookURI = listOfElements.get(i);
			aMapOfBookRankings.put(strBookURI, scoreList[i]);

		}
		
		//Sort the map by value
		aMapOfBookRankings = sortByValue(aMapOfBookRankings);
		//Writed the sorted map in a file
		String outputFile = ProjectVariables.strdataFolder+File.separator+
				ProjectVariables.strUserResultFolder+File.separator+fileName;
		printRankedBooks(aMapOfBookRankings, outputFile);
		
		
		
	}
	
	/**
	 * 
	 * This method will create a sorted map based on the vlaue
	 * 
	 * @param map 
	 * @return Sorted map based on the value 
	 */
	
	public static Map<String, Double> sortByValue(Map<String, Double> map) {
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {

            public int compare(Map.Entry<String, Double> m1, Map.Entry<String, Double> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });

        Map<String, Double> result = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
	
	
	/**
	 * 
	 * Given a map this method will write key values in to tab seperated file
	 * 
	 * @param map 
	 * @param file name
	 * @return 
	 */
	public void printRankedBooks(Map<String, Double> aMapOfRankedBooks, String iFileName){
		
		FileWriter outFile;
		try {
			outFile = new FileWriter(iFileName);
			PrintWriter out = new PrintWriter(outFile);
			Iterator ite = aMapOfRankedBooks.entrySet().iterator();
			while(ite.hasNext()){
				Map.Entry mEntry = (Map.Entry) ite.next();
				String strBookURI = (String) mEntry.getKey();
				double dValue = (double)mEntry.getValue();
				out.println(strBookURI+"\t"+dValue);
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
