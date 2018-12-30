package com.wixsite.jingmacv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This class provides database variables and methods for other classes.
 */
public class OracleDao {
	
	// Transaction objects.
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/*
	 * Initializes a JDBC connection and provides transaction objects.
	 */
	public OracleDao() {
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
	public void closeConnection() {
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
	public void closeStatement() {
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
	public void closePreparedStatement() {
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
	public void closeResultSet() {
		if (rs != null)
			try {
	    		rs.close();
	    	} catch (SQLException se) {
	    		se.printStackTrace();
    	}
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
}
