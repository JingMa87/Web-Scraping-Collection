package com.wixsite.jingmacv;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CpsScraperSelenium {

	public static WebDriver driver;
	public static WebDriverWait wait;

	public static void scrape() {
		// Initializes web driver.
		init("https://dae.ceps.cz/DAEF-GUI/SUC/001System/UCLogin/LoginTest.aspx");
		// Navigates to the page with the data.
		navigateWebsite();
		// Finds all the data and prints it.
		parseTable();
		// Closes driver.
		//driver.close();
	}

	private static void init(String url) {
		System.setProperty("webdriver.ie.driver", "src\\main\\resources\\browser drivers\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		driver.get(url);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		handleSecurityWindow();
		//driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 2000);
	}

	private static void handleSecurityWindow() {
		
	}

	private static void navigateWebsite() {
		// Clicks the Public Access link.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#ctl00_cphUrl_lbtPublicAccess"))).click();
		// Switches to the iframe.
		driver.switchTo().frame("frameMenu");
		// Expands Transmission services and Views, then clicks on Intraday Allocation Capacities.
		driver.findElement(By.cssSelector("#AjaxTreectl00_cphNavPanelContent_vcTreeNode15899800030226586ExpandIcon")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#AjaxTreectl00_cphNavPanelContent_vcTreeNode15899800030226586InnerPanel div.NodePanel span img"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Intraday Allocation Capacities"))).click();
		// Exits the current iframe and goes down into an iframe.
		driver.switchTo().defaultContent();
		driver.switchTo().frame("frameContent");
		// Clicks the Show view button.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cntControlsBtn"))).click();
		// Clicks the Move all to the right button.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".rlbTransferAllFrom"))).click();
		// Clicks the Ok button.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#ctl00_ContentPlaceHolder1_btnViewGetData"))).click();
		// Find tables.
		driver.findElements(By.cssSelector("table")).forEach(table -> System.out.println(table.getText()));
	}

	private static void parseTable() {
		
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		scrape();
		long stopTime = System.nanoTime();
		System.out.println("DURATION: " + (stopTime - startTime) / 1000000000.0);
	}
}
