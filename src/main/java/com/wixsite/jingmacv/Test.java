package com.wixsite.jingmacv;

import org.openqa.selenium.By;

public class Test extends WebScraperCassandra {

	public static void main(String[] args) {
		init("http://demo.guru99.com/selenium/deprecated.html");
		driver.switchTo().frame("classFrame");
		System.out.println(driver.getPageSource());
        driver.findElement(By.linkText("Deprecated")).click();
        driver.close();
	}
}
