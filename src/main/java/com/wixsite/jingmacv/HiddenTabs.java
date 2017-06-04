package com.wixsite.jingmacv;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class HiddenTabs {
	
	private static WebDriver driver;
	
	public static void scrape() {
		// Initializes web driver.
		initWebDriver("http://www.etf.com/channels/bond-etfs");
		// Clicks the pop-up message.
		driver.findElement(By.cssSelector(".popupCloseButton")).click();
		// Finds all the data and prints it.
		findAndPrintAllData(driver);
		// Closes driver.
		driver.close();
	}
	
	private static WebDriver initWebDriver(String url) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		return driver;
	}
	
	private static void findAndPrintAllData(WebDriver driver) {
		// Scrapes the Fund Basics data.
		List<WebElement> rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-0 tbody tr"));
		System.out.println("Tab 1: Fund Basics");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNameBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".issuerBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".expenseRatioBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".aumBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".spreadPercentageBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".segmentBasic")).getAttribute("innerText") + "\n");
		}
		System.out.println();

		// Scrapes the Performance data.
		rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-1 tbody tr"));
		System.out.println("Tab 2: Performance");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNamePerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".oneMonthPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".threeMonthPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".oneYearPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".fiveYearPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".tenYearPerformance")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".asOfPerformance")).getAttribute("innerText") + "\n");			
		}
		System.out.println();

		// Scrapes the Analysis data.
		rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-2 tbody tr"));
		System.out.println("Tab 3: Analysis");
		for (WebElement row : rows) {
			System.out.print(row.findElement(By.cssSelector(".symbolAnalysis")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".fundNameAnalysis")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".issuerAnalysis")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".segmentBasic")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".iulettergradeAnalysis")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".efficiencyScoreAnalysis")).getAttribute("innerText") + ", ");
			System.out.print(row.findElement(By.cssSelector(".tradabilityScoreAnalysis")).getAttribute("innerText") + ", ");	
			System.out.print(row.findElement(By.cssSelector(".fitScoreAnalysis")).getAttribute("innerText") + "\n");		
		}
	}

	public static void main(String[] args) {
		scrape();
	}
}
