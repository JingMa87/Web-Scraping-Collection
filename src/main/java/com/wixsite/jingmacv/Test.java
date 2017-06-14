package com.wixsite.jingmacv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.datastax.driver.core.LocalDate;

public class Test extends WebScraperCassandra {

	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
		try {
			date = df.parse("06/08/2017");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("Date: " + date);
		LocalDate ld = LocalDate.fromMillisSinceEpoch(date.getTime());
		System.out.println("LocalDate: " + ld);
		date = new Date(ld.getMillisSinceEpoch());
		System.out.println("Date: " + date);
//		DBUtilCassandra.initConnection();
//		prepared = session.prepare("INSERT INTO wsc_performance (ticker, fund_name, one_month, three_month, one_year, " + 
//										  "five_year, ten_year, as_of) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
//		bound = prepared.bind();
//		for (int i = 0; i < 8; i++) {
//			if (i == 7)
//				bound.setDate(i, toDate("05/18/2017"));
//			else
//				bound.setString(i, "test" + i);
//		}
//		session.execute(bound);
//		DBUtilCassandra.closeAll();
	}
}
