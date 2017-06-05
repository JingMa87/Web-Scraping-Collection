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
		loopOverTabsAndPrintPages();
		// Closes driver.
		driver.close();
	}
	
	/*
	 * Loops over all the hidden tabs and prints all the pages per tab.
	 */
	private static void loopOverTabsAndPrintPages() {
		// Loops over the tabs.
		for (int i = 0; i <= 6; i++) {
			System.out.println("Tab " + (i + 1) + ": ");
			int count = 0;
			// Loops over the pages.
			while (true) {
				System.out.println("Page: " + ++count);
				printTablePage("#quicktabs-tabpage-tabs-" + Integer.toString(i) + " tbody tr");
				if (driver.findElements(By.cssSelector(".nextPageActive")).size() == 0)
					break;
				clickNextPage();
			}
			resetPage();
		}
	}
	
	/*
	 * Prints the data of a table page.
	 */
	private static void printTablePage(String css) {
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
	private static void clickNextPage() {
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
