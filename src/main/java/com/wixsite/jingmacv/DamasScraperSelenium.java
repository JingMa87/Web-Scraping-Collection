package com.wixsite.jingmacv;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/*
 * This class scrapes Damas data on transmissions.
 */
public class DamasScraperSelenium {

	private static WebDriver driver;
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() {
		// Initializes web driver.
		init("https://damasifa.unicorn.eu/DMSPCAS22.asp");
		// Navigates to the page with the data.
		driver.findElement(By.cssSelector(".tlacitko-vel2")).click();
		// Finds all the data and prints it.
		parseTable();
		// Closes driver.
		driver.close();
	}
	
	/*
	 * Initializes a Internet Explorer web driver that represents a website DOM. 
	 */
	private static void init(String url) {
		System.setProperty("webdriver.gecko.driver", "C:\\Program Files (x86)\\geckodriver-v0.18.0-win32\\geckodriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		driver = new FirefoxDriver(capabilities);
		driver.get(url);
	}

	
	/*
	 * Parses the data and prints it.
	 */
	private static void parseTable() {
		WebElement tbody = driver.findElements(By.cssSelector("tbody")).get(27);
		List<WebElement> rowsUnfiltered = tbody.findElements(By.cssSelector("tr"));
		List<WebElement> rows = IntStream.range(0, rowsUnfiltered.size())
		         .filter(i -> i > 1 && i < 50)
		         .mapToObj(i -> rowsUnfiltered.get(i))
		         .collect(Collectors.toList());
		List<WebElement> fields = rows.stream().map(row -> row.findElements(By.cssSelector("td"))).flatMap(td -> td.stream()).collect(Collectors.toList());
		fields.forEach(field -> System.out.println(field.getText()));
	}
	
	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		scrape();
		long stopTime = System.nanoTime();
		System.out.println("DURATION: " + (stopTime - startTime) / 1000000000.0); // 9 seconds
	}
}
