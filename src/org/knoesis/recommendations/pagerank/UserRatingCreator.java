package org.knoesis.recommendations.pagerank;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.knoesis.utils.ProjectVariables;
import org.knoesis.utils.Serializer;

/**
 * This class is used to create the user profile vector for each user
 * 
 * @author Sarasi
 *
 */
public class UserRatingCreator {
	
	
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserRatingCreator userRatings = new UserRatingCreator();
		File aFile = new File(ProjectVariables.strdataFolder+File.separator
				+ProjectVariables.strUserFolder+File.separator+"5828");
		userRatings.readUserFileRatings(aFile);

	}*/
	
	/**
	 * . 
	 * This is the main method for creating the user profile vector
	 * 
	 * @param filename contains user profiles
	 * @return User Rating Vector which has normalized ratings for each book 
	 */
	
	public Map<String, Double> userProfileGenerator(File fileName){
		
		Map<String, Integer> mapOfUserRatings = readUserFileRatings(fileName);
		Map<String, Double> mapOfUserRatingsDoub = createUserRatingVector(mapOfUserRatings);
		return mapOfUserRatingsDoub;
	}
	
	/**
	 * . 
	 * This method will create the user profile vector which has user ratings for each book
	 * 
	 * @param HashMap contains the integer ratings for each book per user
	 * @return User Rating Vector which has normalized ratings for each book 
	 */
	
	private Map<String, Double> createUserRatingVector(Map<String, Integer> mapUserRatings){
		
		//double[] dUserRatings = new double[listOfElements.size()];
		Map<String, Double> mapOfUserRatings = new HashMap<String, Double>();
		
		Iterator it = mapUserRatings.entrySet().iterator();
		int iRatingSum = 0;
		while(it.hasNext()){		
			Map.Entry pairs = (Map.Entry)it.next();
			iRatingSum = iRatingSum + (Integer)pairs.getValue();
		}
		
		Iterator iteSecond = mapUserRatings.entrySet().iterator();
		while(iteSecond.hasNext()){
			Map.Entry pairs = (Map.Entry)iteSecond.next();
			String strBookURI = (String)pairs.getKey();
			int iRating = (Integer)pairs.getValue();
			double dNormalizedRating = 0.0;
			if(iRatingSum != 0){
				dNormalizedRating = (double)iRating / (double)iRatingSum;
			}
			
			mapOfUserRatings.put(strBookURI, dNormalizedRating);
		}
		
		return mapOfUserRatings;
		
	}
	
	/**
	 *  
	 * This method will read the user profile files and store it in a hashmap
	 * 
	 * @param An inputFile name
	 * @return A hashmap with book uris and rating 
	 */
	
	private Map<String, Integer> readUserFileRatings(File fileName){
		
		Map<String, Integer> aMapOfRatings = new HashMap<String, Integer>();
		try {
			
			Scanner aScan = new Scanner(fileName);
			while(aScan.hasNextLine()){
				String aLine = aScan.nextLine().trim();
				String[] items = aLine.split("\t");
				aMapOfRatings.put(items[0].trim(),Integer.parseInt((items[1].trim())));
			}
			aScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return aMapOfRatings;
		
	}

}
