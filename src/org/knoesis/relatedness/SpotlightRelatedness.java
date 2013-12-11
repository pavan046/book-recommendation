package org.knoesis.relatedness;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.knoesis.api.HttpConnector;
/**
 * This class uses Dbpedia Spotlight's relatedness measure.
 * However this class is not similar to the other classes that 
 * implements ConceptSemanticRelatednessMeasure because, it returns 
 * the top N most relevant entities. 
 * 
 * @author Pavan
 *
 */
public class SpotlightRelatedness {
	private HttpConnector connector;
	/**
	 * The constructor expects api url for getting the 
	 * related entities. 
	 * 
	 * @param apiURL
	 */
	public SpotlightRelatedness(String apiURL) {
		connector = new HttpConnector(apiURL);
	}
	
	/**
	 * This is the main function to call for this class. 
	 * Given an entity, the function returns the most related 
	 * entities with appropriate score. 
	 * 
	 * @param entity1
	 * @param normalize
	 * @return
	 */
	private Map<String, Double> getRelatedEntities(String entity1, boolean normalize){
		Map<String, Double> relatedEntities = new HashMap<String, Double>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("uri", entity1); 
		// FIXME: This parameter is hard coded now. This should be a variable
		params.put("n", "20");
		String jsonResponse = connector.response(params, false);
		relatedEntities = serializeJsonResponse(jsonResponse);
		//This should normalize the values of related entities
		if(normalize)
			normalize(relatedEntities);
		return relatedEntities;
	}

	/**
	 * Normalizes entity relatedness based on the max since 
	 * Spotlight does not provide any kind of normalization
	 * @param relatedEntities
	 */
	private void normalize(Map<String, Double> relatedEntities) {
		double max = 0;
		//Check max
		for(String entity: relatedEntities.keySet()){
			if(relatedEntities.get(entity) > max)
				max = relatedEntities.get(entity);
		}
		//Normalize
		Map<String, Double> temp = new HashMap<String, Double>(relatedEntities);
		for(String entity: temp.keySet()){
			relatedEntities.put(entity, temp.get(entity)/max);
		}
	}

	/**
	 * This function serializes the json response to a 
	 * Map of entity and its relatedness measure. 
	 *
	 * @param json
	 * @return
	 */
	private Map<String, Double> serializeJsonResponse(String json){
		System.out.println(json);
		Map<String, Double> serializedEntities = new HashMap<String, Double>();
		try {
			JSONArray array = new JSONArray(json);
			for(int i=0; i<array.length(); i++){
				JSONObject obj = array.getJSONObject(i);
				Iterator<String> keyIterator = obj.keys();
				while(keyIterator.hasNext()){
					String entity = keyIterator.next();
					double value = obj.getDouble(entity); 
					serializedEntities.put(entity, value);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return serializedEntities; 
	}

	public static void main(String[] args) {
		SpotlightRelatedness relatedness = new SpotlightRelatedness("http://spotlight.dbpedia.org/related/");
		Map<String, Double> relatedEntites = relatedness.getRelatedEntities("Adidas", true);
		System.out.println(relatedEntites);
	}
	

}
