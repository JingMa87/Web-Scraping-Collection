package com.wixsite.jingmacv.scrapers;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wixsite.jingmacv.dao.OracleDao;

/*
 * This class deals with data on hidden tabs. 
 * To scrape hidden data, you need to use the .getAttribute("innerText") or .getAttribute("textContent") method.
 */
public class HiddenTabsOracleScraper extends WebScraper {
	
	private static final OracleDao oracleDao = new OracleDao();
	
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
	}
	
	/*
	 * Resets the table.
	 */
	public static void resetTable(int tableIndex) {
		// Deletes all data from the table.
		try {
			oracleDao.setStmt(oracleDao.getConn().createStatement());
			if (tableIndex == 0)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_fund_basics");
			else if (tableIndex == 1)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_performance");
			else if (tableIndex == 2)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_analysis");
			else if (tableIndex == 3)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_fundamentals");
			else if (tableIndex == 4)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_classification");
			else if (tableIndex == 5)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_tax");
			else if (tableIndex == 6)
				oracleDao.getStmt().executeQuery("DELETE FROM wsc_esg");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	    	// Closes all transaction objects.
		    oracleDao.closeStatement();;
	    }
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
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_fund_basics (ticker, fund_name, issuer, " + 
										  "expense_ratio, aum, spread, segment) VALUES (?, ?, ?, ?, ?, ?, ?)"));
			for (int i = 0; i < fields.size(); i++) {
				oracleDao.getPstmt().setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}		
	}
	
	/*
	 * Saves a row to the wsc_performance table.
	 */
	private static void savePerformanceRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_performance (ticker, fund_name, one_month, three_month, one_year, " + 
										  "five_year, ten_year, as_of) VALUES (?, ?, ?, ?, ?, ?, ?, to_date(?, 'MM/dd/yyyy'))"));
			for (int i = 0; i < fields.size(); i++) {
				oracleDao.getPstmt().setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
	}
	
	/*
	 * Saves a row to the wsc_analysis table.
	 */
	private static void saveAnalysisRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_analysis (ticker, fund_name, issuer, segment, " + 
										  "grade, e, t, f) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"));
			int fieldIndex = 1;
			for (int i = 0; i < fields.size(); i++) {
				if (i == 4)
					continue;
				// If the next 3 values are not integers, null gets saved in the database instead.
				if (fieldIndex == 6 || fieldIndex == 7 || fieldIndex == 8)
					setIntOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
				else
					oracleDao.getPstmt().setString(fieldIndex, fields.get(i).getAttribute("innerText"));
				fieldIndex++;
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
	}
	
	/*
	 * Saves a row to the wsc_fundamentals table.
	 */
	private static void saveFundamentalsRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_fundamentals (ticker, fund_name, dividend_yield, pe, " + 
										  "pb, duration, credit_quality, ytm) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"));
			int fieldIndex = 1;
			// fields.size() - 1 is for the extra field in the List that contains "".
			for (int i = 0; i < fields.size() - 1; i++) {
				if (i == 5)
					continue;
				// If the value is not a float, null gets saved in the database instead.
				if (fieldIndex == 6)
					setFloatOrNull(fieldIndex, fields.get(i).getAttribute("innerText"));
				else
					oracleDao.getPstmt().setString(fieldIndex, fields.get(i).getAttribute("innerText"));
				fieldIndex++;
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
	}
	
	/*
	 * Saves a row to the wsc_classification table.
	 */
	private static void saveClassificationRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_classification (ticker, fund_name, asset_class, strategy, region, " + 
					  					  "geography, category, focus, niche, inverse, leveraged, etn, underlying_index, " +
					  					  "index_provider, selection_criteria, weighting_scheme, active_per_sec) " +
										  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"));
			for (int i = 0; i < fields.size(); i++) {
				// The first two fields are switched around to match database columns.
				if (i == 0)
					oracleDao.getPstmt().setString(i + 1, fields.get(1).getAttribute("innerText"));
				else if (i == 1)
					oracleDao.getPstmt().setString(i + 1, fields.get(0).getAttribute("innerText"));
				else
					oracleDao.getPstmt().setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
}
	
	/*
	 * Saves a row to the wsc_tax table.
	 */
	private static void saveTaxRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_tax (ticker, fund_name, legal_structure, max_lt_capital_gains_rate, " + 
										  "max_st_capital_gains_rate, tax_reporting) VALUES (?, ?, ?, ?, ?, ?)"));
			for (int i = 0; i < fields.size(); i++) {
				oracleDao.getPstmt().setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
	}

	/*
	 * Saves a row to the wsc_esg table.
	 */
	private static void saveEsgRow(List<WebElement> fields) {
		try {
			oracleDao.setPstmt(oracleDao.getConn().prepareStatement("INSERT INTO wsc_esg (ticker, fund_name, msci_esg_quality_score, " + 
										  "esg_score_peer_rank, esg_score_global_rank, carbon_intensity, " + 
										  "sustainable_impact_exposure, sri_screening_crit_exposure) " +
										  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"));
			for (int i = 0; i < fields.size(); i++) {
				oracleDao.getPstmt().setString(i + 1, fields.get(i).getAttribute("innerText"));
			}
			oracleDao.getPstmt().executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			oracleDao.closePreparedStatement();
			oracleDao.closeConnection();
		}
	}
	
	/*
	 * Checks if the string is an int, otherwise sets a null value in the database.
	 */
	private static void setIntOrNull(int index, String text) throws SQLException {
		if (NumberUtils.isCreatable(text))
			oracleDao.getPstmt().setInt(index, Integer.parseInt(text));
		else
			oracleDao.getPstmt().setNull(index, Types.INTEGER);
	}
	
	/*
	 * Checks if the string is a float, otherwise sets a null value in the database.
	 */
	private static void setFloatOrNull(int index, String text) throws SQLException {
		if (NumberUtils.isCreatable(text))
			oracleDao.getPstmt().setFloat(index, Float.parseFloat(text));
		else
			oracleDao.getPstmt().setNull(index, Types.INTEGER);
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
