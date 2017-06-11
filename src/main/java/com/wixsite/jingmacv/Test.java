package com.wixsite.jingmacv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.datastax.driver.core.LocalDate;

public class Test extends WebScraperCassandra {

	public static void main(String[] args) {
		DBUtilCassandra.initConnection();
		prepared = session.prepare("INSERT INTO wsc_performance (ticker, fund_name, one_month, three_month, one_year, " + 
										  "five_year, ten_year, as_of) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		bound = prepared.bind();
		for (int i = 0; i < 8; i++) {
			if (i == 7)
				bound.setDate(i, toDate("06/08/2017"));
			else
				bound.setString(i, "test" + i);
		}
		session.execute(bound);
		DBUtilCassandra.closeAll();
	}
	
	private static LocalDate toDate(String inputDate) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
		try {
			date = df.parse(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDate localDate = LocalDate.fromMillisSinceEpoch(date.getTime());
        return localDate;
	}
}
