package org.knoesis.userprofile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Reads the user profile from a TSV file with 
 * the following format 
 * 
 * <user> <wiki-uri> <rating>
 * @author Pavan
 *
 */
public class TSVUserProfileGenerator {
	private static File userProfileFile; 
	/**
	 * expects the tsv file location of the user profile
	 * <user> <wiki-uri> <rating>
	 * @param fileName
	 */
	public TSVUserProfileGenerator(String fileName) {
		userProfileFile = new File(fileName);
	}
	/**
	 * Generates the user Profile based on the file
	 * @return
	 */
	public Map<String, Double> generateUserProfile(){
		Scanner scan;
		Map<String, Double> userProfile = new HashMap<String, Double>();
		try {
			scan = new Scanner(userProfileFile);
			while(scan.hasNext()){
				String line = scan.nextLine();
				String[] parts = line.split("\t");
				String bookName = parts[1];
				if(parts[1].contains("en.wikipedia"))
					bookName = parts[1].replace("http://en.wikipedia.org/wiki/", "");
				if(parts[1].contains("dbpedia.org"))
					bookName = parts[1].replace("http://dbpedia.org/resource/", "");
				userProfile.put(bookName, Double.parseDouble(parts[2]));
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userProfile;
	}
}
