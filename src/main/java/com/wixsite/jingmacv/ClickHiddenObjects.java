package com.wixsite.jingmacv;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * This class deals with clicking a hidden object. 
 * To click, you need to use a JavascriptExecutor class.
 */
public class ClickHiddenObjects {
	
	private static WebDriver driver;
	private static JavascriptExecutor js;
	
	/*
	 * The only public method in this class. Scrapes a website for data.
	 */
	public static void scrape() {
		// Initializes web driver.
		initWebDriver("http://www.etf.com/channels/bond-etfs");
		// Clicks the pop-up message.
		driver.findElement(By.cssSelector(".popupCloseButton")).click();
		// Clicks on the next pages and prints the tab name.
		clickNextLoop();
		// Closes driver.
		driver.close();
	}

	/*
	 * Initializes a Chrome web driver that represents a website DOM. 
	 */
	private static WebDriver initWebDriver(String url) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		js = (JavascriptExecutor) driver;
		return driver;
	}
	
	private static void clickNextLoop() {
		// Waits one second so the user can scroll down to the table with the tabs.
		waitOneSec();
		
		// Finds and prints the tab titles.
		WebElement tab;
		for (int i = 0; i <= 6; i++) {
			tab = driver.findElement(By.cssSelector("#quicktabs-tab-tabs-" + i));
			if (i != 0)
				js.executeScript("arguments[0].click();", tab);
			System.out.println(tab.getText());			
			// Waits one second between the iterations so the user can see the clicks.
			waitOneSec();
		}
	}
	
	/*
	 * Waits one second.
	 */
	private static void waitOneSec() {
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
