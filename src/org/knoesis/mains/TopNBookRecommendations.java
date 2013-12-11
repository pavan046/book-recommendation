package org.knoesis.mains;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.knoesis.dbpedia.EntityTypeChecker;
import org.knoesis.recommendations.mergemetric.SimpleMerger;
import org.knoesis.relatedness.SpotlightRelatedness;
import org.knoesis.userprofile.DummyUserProfile;
import org.knoesis.utils.ValueComparator;

/**
 * This is the main class that calls the necessary classes to get 
 * the Top N Recommendation.
 * 
 * @author Pavan
 *
 */
public class TopNBookRecommendations {
	public static void main(String[] args) {
		SpotlightRelatedness spotlightRelatedness = new SpotlightRelatedness("http://spotlight.dbpedia.org/related/");
		// TODO: Entity Type Checker
		EntityTypeChecker entityTypeCheck = new EntityTypeChecker();
		// TODO: User Profilles 
		DummyUserProfile userProfileGenerator = new DummyUserProfile();
		Map<String, Double> userBookProfile = new HashMap<String, Double>();
		Map<String, Double> finalRecommendations = new HashMap<>();
		SimpleMerger merger;
		// TODO: Read the user profile
		
		// Initializing merger
		userBookProfile = userProfileGenerator.generateUserProfile();
		merger = new SimpleMerger(userBookProfile.size());
		// For each of the books call the spotlight relatedness to get the top books
		for(String book: userBookProfile.keySet()){
			Map<String, Double> relatedEntities = 
					spotlightRelatedness.getRelatedEntities(book, true, 200);
			Map<String, Double> relatedBooks = new HashMap<>();
			for(String entity: relatedEntities.keySet()){
				if(entityTypeCheck.isABookType(entity))
					relatedBooks.put(entity, relatedEntities.get(entity));
			}
			// Perform a function to merge the recommendations based on all books
			System.out.println(book +":" + relatedBooks.size());
			merger.merge(relatedBooks);
		}
		finalRecommendations = merger.getFinalRecommendations();
		ValueComparator bvc =  new ValueComparator(finalRecommendations);
        TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
        sorted_map.putAll(finalRecommendations);
		System.out.println(sorted_map);
		System.out.println(merger.getFinalRecommendations().size());
	}
}
