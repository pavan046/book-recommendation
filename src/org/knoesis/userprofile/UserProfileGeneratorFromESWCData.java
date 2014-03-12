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
	/**
	 * Constructor to parse the data. 
	 * 
	 * TODO: add another constructor that takes in the file names. 
	 * 				as of now the file names are static.
	 */
	public UserProfileGeneratorFromESWCData() {
		this.parseBookidDbpediaMap();
		this.parseUserProfile();	
	}
	/**
	 * This function parses the user profile file to extract 
	 * user, book, rating 
	 */
	private static void parseUserProfile(){
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(
					new File("data/eswc-challenge-datasets/ratings/DBbook_train_ratings.tsv")));
			String line = null; 
			boolean first = true;
			while((line = fileReader.readLine()) != null){	
				//tsv seperate
				//FIXME: Stupid hack -- for first line
				if(first){
					first=false;
					continue;
				}
				Map<String, Double> profile = new HashMap<String, Double>(); 
				String[] userData = line.split("\t");
				if(userProfiles.keySet().contains(userData[0]))
					profile = userProfiles.get(userData[0]);
				profile.put(bookidsDbpediaUrls.get(userData[1]), Double.parseDouble(userData[2]));
				userProfiles.put(userData[0], profile);
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creates the map of bookid and dbpedia url
	 */
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
				bookidsDbpediaUrls.put(bookid, dbpediaURL.replace("http://dbpedia.org/resource/", ""));
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, Double> generateUserProfile(String userId){
		return userProfiles.get(userId);
	}

	public Set<String> getUserIds(){
		return userProfiles.keySet();
	}

	public static void main(String[] args) {
		UserProfileGeneratorFromESWCData userProfileGen = 
				new UserProfileGeneratorFromESWCData();
		System.out.println(userProfileGen.generateUserProfile("6873"));

	}

}
