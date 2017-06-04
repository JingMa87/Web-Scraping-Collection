package com.wixsite.jingmacv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVParser {

	public static void parseCSV() {
	    String line = "";
	    BufferedReader br = null;
	    try {	
	    	br = new BufferedReader(new FileReader("dataFiles/data.csv"));
	        while ((line = br.readLine()) != null) {	
	            // Splits the line into comma separated values.
	            String[] values = line.split(",");	
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
	
	public static void main(String[] args) {
		parseCSV();
	}
}
