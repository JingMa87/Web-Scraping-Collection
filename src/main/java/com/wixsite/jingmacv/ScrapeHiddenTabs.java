package com.wixsite.jingmacv;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * This class deals with data on hidden tabs. 
 * To scrape hidden data, you need to use the .getAttribute("innerText") or .getAttribute("textContent") method.
 */
public class ScrapeHiddenTabs extends WebScraper {
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() {
		// Initializes web driver.
		init("http://www.etf.com/channels/bond-etfs");
		// Clicks the pop-up message.
		driver.findElement(By.cssSelector(".popupCloseButton")).click();
		// Finds all the data and prints it.
		findAndPrintAllData();
		// Closes driver.
		driver.close();
	}
	
	/*
	 * Searches all hidden tabs and loops over them.
	 */
	private static void findAndPrintAllData() {
		// Scrapes the Fund Basics data.
		System.out.println("Tab 1: Fund Basics");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-0 tbody tr");

		// Scrapes the Performance data.
		System.out.println("Tab 2: Performance");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-1 tbody tr");

		// Scrapes the Analysis data.
		System.out.println("Tab 3: Analysis");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-2 tbody tr");

		// Scrapes the Fundamentals data.
		System.out.println("Tab 4: Fundamentals");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-3 tbody tr");

		// Scrapes the Classification data.
		System.out.println("Tab 5: Classification");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-4 tbody tr");

		// Scrapes the Tax data.
		System.out.println("Tab 6: Tax");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-5 tbody tr");

		// Scrapes the ESG data.
		System.out.println("Tab 7: ESG");
		findTableAndLoopOverPages("#quicktabs-tabpage-tabs-6 tbody tr");
	}
	
	/*
	 * Loops over all the pages of a hidden tab and prints the data.
	 */
	private static void findTableAndLoopOverPages(String css) {
		int count = 0;
		while (true) {
			System.out.println("Page: " + ++count);
			printTable(css);
			if (driver.findElements(By.cssSelector(".nextPageActive")).size() == 0)
				break;
			clickNext();
		}
		resetPage();
	}
	
	/*
	 * Prints the data of a table.
	 */
	private static void printTable(String css) {
		List<WebElement> rows = driver.findElements(By.cssSelector(css));
		for (WebElement row : rows) {
			List<WebElement> fields = row.findElements(By.tagName("td"));
			for (WebElement field : fields) {
				System.out.print(field.getAttribute("innerText") + ", ");
			}
			System.out.println();	
		}
		System.out.println();
	}
	
	/*
	 * Clicks to the next page and waits for it to load.
	 */
	private static void clickNext() {
		driver.findElement(By.cssSelector("#nextPage")).click();
		// Waits for the data to load.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".symbolBasic")));
	}
	
	/*
	 * Resets the page index from 7 to 1.
	 */
	private static void resetPage() {
		WebElement pageField = driver.findElement(By.cssSelector("#goToPage"));
		pageField.clear();
		pageField.sendKeys("1");
		pageField.sendKeys(Keys.RETURN);
		// Waits for the data to load.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".symbolBasic")));
	}

	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		scrape();
	}
}
