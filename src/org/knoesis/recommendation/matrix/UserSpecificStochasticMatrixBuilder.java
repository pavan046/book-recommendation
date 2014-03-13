package org.knoesis.recommendation.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserSpecificStochasticMatrixBuilder implements MatrixBuilder{

	private static String userProfileFile, matrixFileName; 
	private static Set<String> userProfileEntities; 
	private static Map<String, Double> sumForStochasticEntities = new HashMap<>(); 
	private static double threshold = 0.05;
	/**
	 * Needs the user 
	 * @param userProfileFile
	 * @param matrixFileName
	 */
	public UserSpecificStochasticMatrixBuilder(String userProfileFile, String matrixFileName) {
		this.matrixFileName = matrixFileName; 
		this.userProfileFile = userProfileFile; 
		//for now 
		this.matrixFileName = "data/dbpedia/nier-3-dec.tsv"; 
		this.userProfileFile = "data/dbpedia/706";
		readUserProfile(); 
		//For now 
		//this.matrixFileName = ProjectVariables.strdataFolder+File.separator
		//+ProjectVariables.strInputSimilarityFolder+File.separator+ProjectVariables.inputSimilarityFile;
	}
	/**
	 * 
	 */
	private void readUserProfile() {
		userProfileEntities = new HashSet<>();
		BufferedReader read;
		String line=null; 
		try {
			read = new BufferedReader(new FileReader(this.userProfileFile));
			while((line = read.readLine())!=null){
				String[] objects = line.split("\t");
				userProfileEntities.add("<"+objects[0].trim()+">"); 
			}
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, HashMap<String, Double>> mapMatrixBuilder() {
		String strFileName = this.matrixFileName;
		Map<String, HashMap<String, Double>> matrix = new HashMap<String, HashMap<String,Double>>();
		Set<String> allEntities = getAllIUserEntities(); 
		for(String entity: allEntities){
			HashMap<String, Double> relatedBooks = new HashMap<>();
			sumForStochasticEntities.put(entity, 0.0d);
			matrix.put(entity, relatedBooks); 
		}
		BufferedReader read;

		try {
			read = new BufferedReader(new FileReader(strFileName));
			String line = null;
			int i=0;

			while((line = read.readLine())!=null){
				i++;
				String[] objects = line.split("\t");
				double score = Double.parseDouble(objects[2]);
				if(score>threshold){
					if(matrix.keySet().contains(objects[0])){
						matrix.get(objects[0]).put(objects[1], score);
						double sum = sumForStochasticEntities.get(objects[0]) + score;
						sumForStochasticEntities.put(objects[0], sum);
					}
					if(matrix.keySet().contains(objects[1])){
						matrix.get(objects[1]).put(objects[0], score);
						double sum = sumForStochasticEntities.get(objects[1]) + score;
						sumForStochasticEntities.put(objects[1], sum);
					}
				}
				if(i%1000000 == 0)
					System.out.println(i+": "+matrix.keySet().size()+": "+ System.currentTimeMillis());
			}
			addDanglingEntitiesToMatrix(matrix); 
			makeMatrixStochastic(matrix);
			read.close();
			System.out.println("Total Number of Books: "+matrix.keySet().size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matrix;
	}

	// 
	/**
	 * 
	 * @param matrix
	 */
	private void makeMatrixStochastic(
			Map<String, HashMap<String, Double>> matrix) {
		Set<String> books = new HashSet<String>(matrix.keySet());
		for(String book: books){
			double sum = sumForStochasticEntities.get(book);
			//removing books of user profile that do not have any related books 
			if(matrix.get(book).isEmpty()){
				matrix.remove(book);
				continue;
			}

			Set<String> relatedBooks = new HashSet<>(matrix.get(book).keySet()); 
			for(String relatedBook: relatedBooks){
				matrix.get(book).put(relatedBook, matrix.get(book).get(relatedBook)/sum);
			}
		}
	}
	/**
	 * completing the graph by adding edges to dangling links.
	 * @param matrix
	 */
	private void addDanglingEntitiesToMatrix(
			Map<String, HashMap<String, Double>> matrix) {
		Set<String> danglingLinks = new HashSet<>(); 
		Set<String> entities = new HashSet<>(matrix.keySet()); 
		for(String entity: entities){
			for(String danglingLink: matrix.get(entity).keySet()){
				if(!matrix.keySet().contains(danglingLink)){
					danglingLinks.add(danglingLink); 
					HashMap<String, Double> relatedLinks = new HashMap<String, Double>();
					relatedLinks.put(entity, matrix.get(entity).get(danglingLink)); 
					sumForStochasticEntities.put(danglingLink, matrix.get(entity).get(danglingLink));
					matrix.put(danglingLink, relatedLinks);
				} else if(danglingLinks.contains(danglingLink)){
					double sum = sumForStochasticEntities.get(danglingLink) + matrix.get(entity).get(danglingLink);
					matrix.get(danglingLink).put(entity, matrix.get(entity).get(danglingLink)); 
					sumForStochasticEntities.put(danglingLink, sum);
				}
			}
		}
	}
	/**
	 * Gets all the entities that have to be considered
	 * @return
	 */
	private Set<String> getAllIUserEntities() {
		Set<String> allEntities = new HashSet<>(); 
		try {
			BufferedReader read = new BufferedReader(new FileReader(new File(this.matrixFileName)));
			String line = null; 
			int i=0;
			while((line=read.readLine())!=null){
				i++;
				String objects[] = line.split("\t");
				double score = Double.parseDouble(objects[2]);
				if(score>threshold){
					if(userProfileEntities.contains(objects[0]))
						allEntities.add(objects[1]); 
					else if (userProfileEntities.contains(objects[1]))
						allEntities.add(objects[0]);
				}
				if(i%1000000==0)
					System.out.println("Entities got for this user at " + i +" "+allEntities.size());
			}
			System.out.println("Entities got for this user at " + i +" "+allEntities.size());
			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		allEntities.addAll(userProfileEntities);
		return allEntities;
	}

	public static void main(String[] args) {
		MatrixBuilder matrix = new UserSpecificStochasticMatrixBuilder(args[0], args[1]); 
		matrix.mapMatrixBuilder(); 
	}
}