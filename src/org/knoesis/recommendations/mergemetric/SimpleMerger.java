package org.knoesis.recommendations.mergemetric;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the most simple merger for a user with the following function 
 * 
 * BookScore = Sum(Relateness-Measure)/Total number of books from the user
 *
 * @author Pavan
 *
 */
public class SimpleMerger {
	private int totalNumberOfBooks = 1;
	private static Map<String, Double> finalRecommendations; 
	/**
	 * Expects the user's total number of books
	 * @param totalNumberOfBooks
	 */
	public SimpleMerger(int totalNumberOfBooks) {
		this.totalNumberOfBooks = totalNumberOfBooks;
		finalRecommendations = new HashMap<String, Double>();
	}
	/**
	 * Merges the values of recommended books
	 * @param relatedBooks
	 */
	public void merge(Map<String, Double> relatedBooks){
		for(String book: relatedBooks.keySet()){
			double score = relatedBooks.get(book)/totalNumberOfBooks; 
			if(finalRecommendations.containsKey(book)){
				score+=finalRecommendations.get(book); 
			}
			finalRecommendations.put(book, score);
		}
	}
	/**
	 * Returns the final recommendations
	 * 
	 * TODO: This might have to be sorted
	 * @return
	 */
	public Map<String, Double> getFinalRecommendations(){
		return finalRecommendations;
	}
}
