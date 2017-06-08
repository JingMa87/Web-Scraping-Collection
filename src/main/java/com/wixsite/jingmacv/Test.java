package com.wixsite.jingmacv;

import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Test extends WebScraper {

	public static void main(String[] args) {
		init("http://www.etf.com/channels/bond-etfs");
		List<WebElement> rows = driver.findElements(By.cssSelector("#quicktabs-tabpage-tabs-4 #classificationScrollContainer tbody tr"));
		for (WebElement row : rows) {
			List<WebElement> fields = row.findElements(By.tagName("td"));
			for (WebElement field : fields) {
				System.out.println(field.getAttribute("innerText"));
			}
		}
		driver.close();
		
//		DBUtil.initConnection();
//		try {
//			pstmt = conn.prepareStatement("INSERT INTO wsc_analysis (ticker, fund_name, issuer, segment, " + 
//					  "grade, e, t, f) VALUES ('test', null, null, null, null, null, null, ?)");
//			pstmt.setInt(1, Integer.parseInt("0"));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			DBUtil.close(pstmt);
//			DBUtil.close(conn);
//		}
	}
}
