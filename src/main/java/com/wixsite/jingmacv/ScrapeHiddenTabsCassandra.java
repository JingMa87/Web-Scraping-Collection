package com.wixsite.jingmacv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.datastax.driver.core.LocalDate;

/*
 * This class deals with data on hidden tabs. 
 * To scrape hidden data, you need to use the .getAttribute("innerText") or .getAttribute("textContent") method.
 */
public class ScrapeHiddenTabsCassandra extends WebScraperCassandra {
	
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
		DBUtilCassandra.initConnection();
		// Loops over the tabs.
		for (int i = 1; i <= 1; i++) {
			System.out.println("Tab " + (i + 1));
			// Removes all rows from the table.
			resetTable(i);
			int count = 0;
			// Loops over the pages.
			while (true) {
				System.out.println("Page " + ++count);
				// Inserts all the rows on this page to the database table and clicks to the next page.
				savePageData("#quicktabs-tabpage-tabs-" + i + " tbody tr");
				if (driver.findElements(By.cssSelector(".nextPageActive")).size() == 0)
					break;
				clickNextPage();
			}
			resetPage();
		}
		// Closes the session and cluster objects.
		DBUtilCassandra.closeAll();
	}
	
	/*
	 * Resets the table.
	 */
	public static void resetTable(int tableIndex) {
		// Deletes all data from the table.
		if (tableIndex == 0)
			session.execute("TRUNCATE wsc_fund_basics");
		else if (tableIndex == 1)
			session.execute("TRUNCATE wsc_performance");
		else if (tableIndex == 2)
			session.execute("TRUNCATE wsc_analysis");
		else if (tableIndex == 3)
			session.execute("TRUNCATE wsc_fundamentals");
		else if (tableIndex == 4)
			session.execute("TRUNCATE wsc_classification");
		else if (tableIndex == 5)
			session.execute("TRUNCATE wsc_tax");
		else if (tableIndex == 6)
			session.execute("TRUNCATE wsc_esg");
	}
	
	/*
	 * Prints the data of a table page.
	 */
	private static void savePageData(String css) {
		// Handles the Classification tab separately, since it has a different html.
		if (css.equals("#quicktabs-tabpage-tabs-4 tbody tr")) {
			savePageData("#quicktabs-tabpage-tabs-4 #classificationScrollContainer tbody tr");
			return; // Test if this is necessary!
		}
		List<WebElement> rows = driver.findElements(By.cssSelector(css));
		for (WebElement row : rows) {
			List<WebElement> fields = row.findElements(By.tagName("td"));
			if (css.equals("#quicktabs-tabpage-tabs-0 tbody tr")) {
				saveFundBasicsRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-1 tbody tr")) {
				savePerformanceRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-2 tbody tr")) {
				saveAnalysisRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-3 tbody tr")) {
				saveFundamentalsRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-4 #classificationScrollContainer tbody tr")) {
				saveClassificationRow(fields);
			}			
			else if (css.equals("#quicktabs-tabpage-tabs-5 tbody tr")) {
				saveTaxRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-6 tbody tr")) {
				saveEsgRow(fields);
			}
		}
	}

	/*
	 * Saves a row to the wsc_fund_basics table.
	 */
	private static void saveFundBasicsRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_fund_basics (ticker, fund_name, issuer, " + 
									  "expense_ratio, aum, spread, segment) VALUES (?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < fields.size(); i++) {
			bound.setString(i, fields.get(i).getAttribute("innerText"));
		}
		session.execute(bound);
	}
	
	/*
	 * Saves a row to the wsc_performance table.
	 */
	private static void savePerformanceRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_performance (ticker, fund_name, one_month, three_month, one_year, " + 
										  "five_year, ten_year, as_of) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < fields.size(); i++) {
			if (i == 7)
				bound.setDate(i, toDate(fields.get(i).getAttribute("innerText")));
			else
				bound.setString(i, fields.get(i).getAttribute("innerText"));
		}
		session.execute(bound);
	}
	
	/*
	 * Saves a row to the wsc_analysis table.
	 */
	private static void saveAnalysisRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_analysis (ticker, fund_name, issuer, segment, " + 
									  "grade, e, t, f) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		int fieldIndex = 0;
		for (int i = 0; i < fields.size(); i++) {
			if (i == 4)
				continue;
			// If the next 3 values are not integers, null gets saved in the database instead.
			if (fieldIndex == 5 || fieldIndex == 6 || fieldIndex == 7)
				setIntOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
			else
				bound.setString(fieldIndex, fields.get(i).getAttribute("innerText"));
			fieldIndex++;
		}
		session.execute(bound);
	}
	
	/*
	 * Saves a row to the wsc_fundamentals table.
	 */
	private static void saveFundamentalsRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_fundamentals (ticker, fund_name, dividend_yield, pe, " + 
									  "pb, duration, credit_quality, ytm) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		int fieldIndex = 0;
		// fields.size() - 1 is for the extra field in the List that contains "".
		for (int i = 0; i < fields.size() - 1; i++) {
			if (i == 5)
				continue;
			// If the value is not a float, null gets saved in the database instead.
			if (fieldIndex == 5)
				setDoubleOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
			else
				bound.setString(fieldIndex, fields.get(i).getAttribute("innerText"));
			fieldIndex++;
		}
		session.execute(bound);
	}
	
	/*
	 * Saves a row to the wsc_classification table.
	 */
	private static void saveClassificationRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_classification (ticker, fund_name, asset_class, strategy, region, " + 
				  					  "geography, category, focus, niche, inverse, leveraged, etn, underlying_index, " +
				  					  "index_provider, selection_criteria, weighting_scheme, active_per_sec) " +
									  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < fields.size(); i++) {
			// The first two fields are switched around to match database columns.
			if (i == 0)
				bound.setString(i, fields.get(1).getAttribute("innerText"));
			else if (i == 1)
				bound.setString(i, fields.get(0).getAttribute("innerText"));
			else
				bound.setString(i, fields.get(i).getAttribute("innerText"));
		}
		session.execute(bound);
}
	
	/*
	 * Saves a row to the wsc_tax table.
	 */
	private static void saveTaxRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_tax (ticker, fund_name, legal_structure, max_lt_capital_gains_rate, " + 
									  "max_st_capital_gains_rate, tax_reporting) VALUES (?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < fields.size(); i++) {
			bound.setString(i, fields.get(i).getAttribute("innerText"));
		}
		session.execute(bound);
	}

	/*
	 * Saves a row to the wsc_esg table.
	 */
	private static void saveEsgRow(List<WebElement> fields) {
		prepared = session.prepare("INSERT INTO wsc_esg (ticker, fund_name, msci_esg_quality_score, " + 
									  "esg_score_peer_rank, esg_score_global_rank, carbon_intensity, " + 
									  "sustainable_impact_exposure, sri_screening_crit_exposure) " +
									  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < fields.size(); i++) {
			bound.setString(i, fields.get(i).getAttribute("innerText"));
		}
		session.execute(bound);
	}
	
	/*
	 * Converts a date string to a DataStax LocalDate type.
	 */
	private static LocalDate toDate(String inputDate) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
		try {
			date = df.parse(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH) + 1;
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    return LocalDate.fromYearMonthDay(year, month, day);
	}
	
	/*
	 * Checks if the string is an int, otherwise sets a null value in the database.
	 */
	private static void setIntOrNull(int index, String text) {
		if (NumberUtils.isCreatable(text))
			bound.setInt(index, Integer.parseInt(text));
	}
	
	/*
	 * Checks if the string is a double, otherwise sets a null value in the database.
	 */
	private static void setDoubleOrNull(int index, String text) {
		if (NumberUtils.isCreatable(text))
			bound.setDouble(index, Double.parseDouble(text));
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
		// Logs the DataStax info.
		//BasicConfigurator.configure();
		scrape();
	}
}
