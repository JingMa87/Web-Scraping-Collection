package com.wixsite.jingmacv;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/*
 * This class deals with clicking a hidden object. 
 * To click, you need to use a JavascriptExecutor class.
 */
public class ClickHiddenObjects extends WebScraper {
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() {
		// Initializes web driver.
		init("http://www.etf.com/channels/bond-etfs");
		// Closes the pop-up message.
		driver.findElement(By.cssSelector(".popupCloseButton")).click();
		// Clicks on the next pages and prints the tab name.
		clickNextLoop();
		// Closes driver.
		driver.close();
	}
	
	private static void clickNextLoop() {
		// Waits one second so the user can scroll down to the table with the tabs.
		sleepOneSec();		
		// Finds and prints the tab titles.
		WebElement tab;
		for (int i = 0; i <= 6; i++) {
			tab = driver.findElement(By.cssSelector("#quicktabs-tab-tabs-" + i));
			if (i != 0)
				js.executeScript("arguments[0].click();", tab);
			System.out.println(tab.getText());			
			// Waits one second between the iterations so the user can see the clicks.
			sleepOneSec();
		}
	}
	
	/*
	 * Waits one second.
	 */
	private static void sleepOneSec() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		scrape();
	}
}
