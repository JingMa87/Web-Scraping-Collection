package com.wixsite.jingmacv;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
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
//		for (int i = 0; i <= 6; i++) {
//			System.out.println("Tab " + (i + 1) + ": ");
			int count = 0;
			// Loops over the pages.
			while (true) {
				System.out.println("Page: " + ++count);
				savePageData("#quicktabs-tabpage-tabs-" + 4 + " tbody tr");
				if (driver.findElements(By.cssSelector(".nextPageActive")).size() == 0)
					break;
				clickNextPage();
			}
			resetPage();
//		}
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
				//saveFundBasicsRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-1 tbody tr")) {
				//savePerformanceRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-2 tbody tr")) {
				//saveAnalysisRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-3 tbody tr")) {
				//saveFundamentalsRow(fields);
			}
			else if (css.equals("#quicktabs-tabpage-tabs-4 #classificationScrollContainer tbody tr")) {
				saveClassificationRow(fields);
			}			
			else if (css.equals("#quicktabs-tabpage-tabs-5 tbody tr")) {
				//saveTaxRow(fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5));
			}
			else if (css.equals("#quicktabs-tabpage-tabs-6 tbody tr")) {
				//saveEsgRow(fields.get(0), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5), fields.get(6), fields.get(7));
			}
		}
	}

	/*
	 * Saves a row to the wsc_fund_basics table.
	 */
	private static void saveFundBasicsRow(List<WebElement> fields) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_fund_basics (ticker, fund_name, issuer, " + 
										  "expense_ratio, aum, spread, segment) VALUES (?, ?, ?, ?, ?, ?, ?)");
			for (int i = 0; i < fields.size(); i++) {
				pstmt.setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}		
	}
	
	/*
	 * Saves a row to the wsc_performance table.
	 */
	private static void savePerformanceRow(List<WebElement> fields) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_performance (ticker, fund_name, one_month, three_month, one_year, " + 
										  "five_year, ten_year, as_of) VALUES (?, ?, ?, ?, ?, ?, ?, to_date(?, 'MM/dd/yyyy'))");
			for (int i = 0; i < fields.size(); i++) {
				pstmt.setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}
	}
	
	/*
	 * Saves a row to the wsc_analysis table.
	 */
	private static void saveAnalysisRow(List<WebElement> fields) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_analysis (ticker, fund_name, issuer, segment, " + 
										  "grade, e, t, f) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			int fieldIndex = 1;
			for (int i = 0; i < fields.size(); i++) {
				if (i == 4)
					continue;
				// If the next 3 values are not integers, null gets saved in the database instead.
				if (fieldIndex == 6 || fieldIndex == 7 || fieldIndex == 8)
					setIntOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
				else
					pstmt.setString(fieldIndex, fields.get(i).getAttribute("innerText"));
				fieldIndex++;
			}
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}
	}
	
	/*
	 * Saves a row to the wsc_fundamentals table.
	 */
	private static void saveFundamentalsRow(List<WebElement> fields) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_fundamentals (ticker, fund_name, dividend_yield, pe, " + 
										  "pb, duration, credit_quality, ytm) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			int fieldIndex = 1;
			// fields.size() - 1 is for the extra field in the List that contains "".
			for (int i = 0; i < fields.size() - 1; i++) {
				if (i == 5)
					continue;
				// If the value is not a float, null gets saved in the database instead.
				if (fieldIndex == 6)
					setFloatOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
				else
					pstmt.setString(fieldIndex, fields.get(i).getAttribute("innerText"));
				fieldIndex++;
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}
	}
	
	/*
	 * Saves a row to the wsc_classification table.
	 */
	private static void saveClassificationRow(List<WebElement> fields) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_classification (ticker, fund_name, asset_class, strategy, region, " + 
					  					  "geography, category, focus, niche, inverse, leveraged, etn, underlying_index, " +
					  					  "index_provider, selection_criteria, weighting_scheme, active_per_sec) " +
										  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			for (int i = 0; i < fields.size(); i++) {
				// The first two fields are switched around to match database columns.
				if (i == 0)
					pstmt.setString(i + 1, fields.get(1).getAttribute("innerText"));
				else if (i == 1)
					pstmt.setString(i + 1, fields.get(0).getAttribute("innerText"));
				else
					pstmt.setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}
}
	
	/*
	 * Saves a row to the wsc_tax table.
	 */
	private static void saveTaxRow(WebElement ticker, WebElement fundName, WebElement legalStructure, WebElement maxLTCapitalGainsRate, 
								   WebElement maxSTCapitalGainsRate, WebElement taxReporting) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_tax (ticker, fund_name, legal_structure, max_lt_capital_gains_rate, " + 
										  "max_st_capital_gains_rate, tax_reporting) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setString(1, ticker.getAttribute("innerText"));
			pstmt.setString(2, fundName.getAttribute("innerText"));
			pstmt.setString(3, legalStructure.getAttribute("innerText"));
			pstmt.setString(4, maxLTCapitalGainsRate.getAttribute("innerText"));
			pstmt.setString(5, maxSTCapitalGainsRate.getAttribute("innerText"));
			pstmt.setString(6, taxReporting.getAttribute("innerText"));
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}
	}

	/*
	 * Saves a row to the wsc_esg table.
	 */
	private static void saveEsgRow(WebElement ticker, WebElement fundName, WebElement msciEsgQualityScore, 
								   WebElement esgScorePeerRank, WebElement esgScoreGlobalRank, WebElement carbonIntensity, 
								   WebElement sustainableImpactExposure, WebElement sriScreeningCriteriaExposure) {
		DBUtil.initConnection();
		try {
			pstmt = conn.prepareStatement("INSERT INTO wsc_esg (ticker, fund_name, msci_esg_quality_score, " + 
										  "esg_score_peer_rank, esg_score_global_rank, carbon_intensity, " + 
										  "sustainable_impact_exposure, sri_screening_crit_exposure) " +
										  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setString(1, ticker.getAttribute("innerText"));
			pstmt.setString(2, fundName.getAttribute("innerText"));
			pstmt.setString(3, msciEsgQualityScore.getAttribute("innerText"));
			pstmt.setString(4, esgScorePeerRank.getAttribute("innerText"));
			pstmt.setString(5, esgScoreGlobalRank.getAttribute("innerText"));
			pstmt.setString(6, carbonIntensity.getAttribute("innerText"));
			pstmt.setString(7, sustainableImpactExposure.getAttribute("innerText"));
			pstmt.setString(8, sriScreeningCriteriaExposure.getAttribute("innerText"));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(conn);
		}		
	}
	
	/*
	 * Checks if the string is an int, otherwise sets a null value in the database.
	 */
	private static void setIntOrNull(int index, String text) throws SQLException {
		if (NumberUtils.isCreatable(text))
			pstmt.setInt(index, Integer.parseInt(text));
		else
			pstmt.setNull(index, Types.INTEGER);
	}
	
	/*
	 * Checks if the string is a float, otherwise sets a null value in the database.
	 */
	private static void setFloatOrNull(int index, String text) throws SQLException {
		if (NumberUtils.isCreatable(text))
			pstmt.setFloat(index, Float.parseFloat(text));
		else
			pstmt.setNull(index, Types.INTEGER);
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
