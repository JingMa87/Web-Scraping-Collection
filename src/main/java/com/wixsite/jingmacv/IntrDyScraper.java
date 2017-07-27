package com.wixsite.jingmacv;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * This class scrapes Intraday data on transmissions.
 */
public class IntrDyScraper extends WebScraper {

	public static WebDriver driver;
	public static WebDriverWait wait;
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() {
		// Initializes web driver.
		init("https://git.northpool.pvt:8443/");
		// Navigates to the page with the data.
		navigateWebsite();
		// Finds all the data and prints it.
		parseTable();
		// Closes driver.
		//driver.close();
	}
	
	/*
	 * Navigates to the page with the data.
	 */
	private static void navigateWebsite() {
		
	}
	
	/*
	 * Parses the data and prints it.
	 */
	private static void parseTable() {
		
	}
	
	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		scrape();
		long stopTime = System.nanoTime();
		System.out.println("DURATION: " + (stopTime - startTime) / 1000000000.0); // n seconds
	}
}
