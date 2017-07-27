package com.wixsite.jingmacv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This class provides database variables and methods for other classes.
 */
public abstract class DBUtilOracle extends WebScraper {
	
	// Transaction objects.
	public static Connection conn = null;
	public static Statement stmt = null;
	public static PreparedStatement pstmt = null;
	public static ResultSet rs = null;
	
	/*
	 * Initializes a JDBC connection and provides transaction objects.
	 */
	public static void initConnection() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:jing/jing@localhost:1521:xe");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *  Closes Connection.
	 */
	public static void close(Connection conn) {
		if (conn != null)
			try {
	    		conn.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes Statement.
	 */
	public static void close(Statement stmt) {
		if (stmt != null)
			try {
	    		stmt.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes PreparedStatement.
	 */
	public static void close(PreparedStatement pstmt) {
    	if (pstmt != null)
    		try {
    			pstmt.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
	
	/*
	 *  Closes ResultSet.
	 */
	public static void close(ResultSet rs) {
		if (rs != null)
			try {
	    		rs.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}
}
