package org.knoesis.userprofile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class creates a user profile fromt he dataset provided by ESWC
 * 
 * @author pavan
 *
 */
public class UserProfileGeneratorFromESWCData {
	private static Map<String, String> bookidsDbpediaUrls = 
			new HashMap<String, String>();
	private static Map<String, Map<String, Double>> userProfiles = 
			new HashMap<String, Map<String, Double>>();


	public static void parseUserProfile(){
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(
					new File("data/eswc-challenge-datasets/rating/DBbook_train_ratings.tsv")));
			String line = null; 
			while((line = fileReader.readLine()) != null){	
				//tsv seperate 
				Map<String, Double> profile = new HashMap<String, Double>(); 
				String[] userData = line.split("\t");
				if(userProfiles.keySet().contains(userData[0]))
					profile = userProfiles.get(userData[0]);
				profile.put(bookidsDbpediaUrls.get(userData[1]), Double.parseDouble(userData[2]));
				userProfiles.put(userData[0], profile);
				// add it to userProfileBookids
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseBookidDbpediaMap(){
		try {
			///book-recommendation/data/eswc-challenge-datasets/mapping/DBbook_Items_DBpedia_mapping.tsv
			BufferedReader fileReader = new BufferedReader(
					new FileReader(new File("data/eswc-challenge-datasets/mapping/DBbook_Items_DBpedia_mapping.tsv")));
			String line = null; 
			while((line = fileReader.readLine()) != null){	
				//tsv seperate 
				String[] booksDbpedia = line.split("\t");
				String bookid = booksDbpedia[0];
				String dbpediaURL = booksDbpedia[2];
				bookidsDbpediaUrls.put(bookid, dbpediaURL);
				// add it to userProfileBookids

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, Double> getUserProfile(String userId){
		return userProfiles.get(userId);
	}
	
	public Set<String> getUserIds(){
		return userProfiles.keySet();
	}
	
	public static void main(String[] args) {
		UserProfileGeneratorFromESWCData userProfileGen = 
				new UserProfileGeneratorFromESWCData();
		userProfileGen.parseBookidDbpediaMap();
		userProfileGen.parseUserProfile(); 
		
	}

}
