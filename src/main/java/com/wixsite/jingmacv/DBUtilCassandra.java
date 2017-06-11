package com.wixsite.jingmacv;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/*
 * This class provides database variables and methods for other classes.
 */
public abstract class DBUtilCassandra {
	
	// Transaction objects.
	public static Cluster cluster = null;
	public static Session session = null;
	public static PreparedStatement prepared = null; 
	public static BoundStatement bound = null;
	public static ResultSet result = null;
	
	/*
	 * Initializes a JDBC connection and provides transaction objects.
	 */
	public static void initConnection() {
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("jing");
	}
	
	/*
	 *  Closes the session and the cluster.
	 */
	public static void closeAll() {
		if (session != null)
			session.close();
		if (cluster != null)
			cluster.close();
	}
}
