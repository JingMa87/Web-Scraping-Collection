package com.wixsite.jingmacv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * This class parses a CSV file for data and prints it.
 */
public class CSVParser {

	/*
	 * The only public method in this class. Parses a CSV file for data and prints it.
	 */
	public static void parseCSV() {
	    String line = "";
	    BufferedReader br = null;
	    try {
	    	// Reads the CSV file.
	    	br = new BufferedReader(new FileReader("data files/data.csv"));
	        while ((line = br.readLine()) != null) {	
	            // Splits every line into comma separated values.
	            String[] values = line.split(",");	
	            // Loops over the values and prints them.
	            for (int i = 0; i < values.length; i++) {
	            	System.out.print(values[i]);
	            	if (i != values.length - 1)
	            		System.out.print(", ");
	            }
	            System.out.println();
	        }	
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	/*
	 * Main method.
	 */	
	public static void main(String[] args) {
		parseCSV();
	}
}
