package com.synalogik.wordcountproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads file from project root directory "/fileToRead/"
 * If investing more time to enhance this would replace this with proper file upload service via spring
 * negating the need for file to be placed in specific directory as well as the path call param
 *
 */
public class FileReadUtil {

	/**
	 * Reads entire file as string
	 * 
	 * @param filePath - full file path of text file
	 * @return - File contents as string
	 */
	public static String fileRead(String filePath)
	{
		try {
			Path filename = Path.of(filePath);
			return Files.readString(filename);
		}
		catch(IOException e) {
		return "Failed to read file: " + filePath;
		}
	}
	
	
	/**
	 * Defaults file to read to 'target.txt' if call made with no path param
	 * 
	 * Path input assumes this is placed in the correct 'fileToRead' import directory and is .txt format
	 * 
	 * @param path - file to read (without extension)
	 * @return - Complete file-path to read as string
	 */
	public static String constructPath(String path) 
	{
		if (path != null && !path.isBlank()) 
			return "fileToRead/" + path + ".txt";
		else
			return "fileToRead/target.txt";
	}
}
