package org.knoesis.userprofile;

import java.util.HashMap;
import java.util.Map;
/**
 * This is a dummy user profile that will be generated
 * @author Pavan
 *
 */
public class DummyUserProfile {
	public static Map<String, Double> books = new HashMap<>();
	/**
	 * Returns a dummy user profile
	 * @return
	 */
	public Map<String, Double> generateUserProfile(){
		//books.put("Nineteen_Eighty-Four", 1.0);
		//books.put("Siddhartha_(novel)", 0.76);
		//books.put("Wings_of_Fire", 0.65);
		//books.put("War_and_Peace", 0.83);
		//books.put("The_Da_Vinci_Code", 0.54);
		books.put("The_Eternal_Triangle", 1.0);
		books.put("Outliers_(book)", 2.0);
		books.put("The_Tell-Tale_Brain", 1.0);
		books.put("Bhagavad_Gita_(Sargeant)", 1.0);
		
		return books;
	}
}
