package com.wixsite.jingmacv;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class HiddenTabs {
	
	private static WebDriver driver;
	private static JavascriptExecutor js;
	private static WebElement tab;
	
	public static void scrape() {
		// Initializes web driver.
		initWebDriver("http://www.etf.com/channels/bond-etfs");
		// Clicks the pop-up message.
		driver.findElement(By.cssSelector(".popupCloseButton")).click();
		// Finds all the data and prints it.
		findAndPrintAllData(driver);
	}
	
	private static WebDriver initWebDriver(String url) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		// Executes JavaScript for hidden tabs.
		js = (JavascriptExecutor) driver;
		return driver;
	}
	
	private static void findAndPrintAllData(WebDriver driver) {
		// Scrapes the data off the first tab called Fund Basics.
		List<WebElement> rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-0 tbody tr"));
		System.out.println("Tab 1: Fund Basics");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNameBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".issuerBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".expenseRatioBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".aumBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".spreadPercentageBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".segmentBasic")).getText() + "\n");
		}
		System.out.println();
		
		// Clicks on the Performance tab.
		tab = driver.findElement(By.cssSelector("#quicktabs-tab-tabs-1"));
		js.executeScript("arguments[0].click();", tab);
		// Scrapes the data.
		rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-1 tbody tr"));
		System.out.println("Tab 2: Performance");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNamePerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".oneMonthPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".threeMonthPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".oneYearPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".fiveYearPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".tenYearPerformance")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".asOfPerformance")).getText() + "\n");			
		}
		System.out.println();
		
		// Clicks on the Analysis tab.
		tab = driver.findElement(By.cssSelector("#quicktabs-tab-tabs-2"));
		js.executeScript("arguments[0].click();", tab);
		// Scrapes the data.
		rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-2 tbody tr"));
		System.out.println("Tab 3: Analysis");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNameAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".issuerAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".segmentBasic")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".analystPickAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".iulettergradeAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".efficiencyScoreAnalysis")).getText() + ", ");
			System.out.print(row.findElement(By.cssSelector(".tradabilityScoreAnalysis")).getText() + ", ");	
			System.out.print(row.findElement(By.cssSelector(".fitScoreAnalysis")).getText() + "\n");		
		}
	}

	public static void main(String[] args) {
		scrape();
	}
}
