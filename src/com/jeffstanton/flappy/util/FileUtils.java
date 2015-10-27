package com.jeffstanton.flappy.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	
	private FileUtils() {
	}  // private utility class
	
	public static String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			while((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');  // Step through file appending input to result
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}  // end loadAsString
}  // end FileUtils
