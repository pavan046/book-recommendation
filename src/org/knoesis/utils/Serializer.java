package org.knoesis.utils;

import it.unimi.dsi.fastutil.io.BinIO;
import java.io.File;
import java.io.IOException;

/*******************************************************
 * The serializer serializes and reads generic objects
 */
public class Serializer {

	/*******************************************************************
	 * This method serialzes an object to the output directory given.
	 * @param outputFileName			output path location
	 * @param object					object to be serialized
	 */
	public static void serialize(String outputFileName, Object object){
		
		try {
			File outFile = new File(outputFileName.substring(0, outputFileName.lastIndexOf(File.separator)));
			if(!outFile.exists())
				outFile.mkdirs();
			BinIO.storeObject(object, outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/******************************************************
	 * This method loads a serialized object that has been
	 * compressed using fastutils/BinIO
	 * @param inputFilePath		path to serialized object
	 * @return					serialized object by type
	 */
	public static Object load(String inputFilePath){
		try {
			Object serializedObject = BinIO.loadObject(inputFilePath);
			return serializedObject;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
