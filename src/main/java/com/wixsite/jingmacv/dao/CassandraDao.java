package com.wixsite.jingmacv.dao;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/*
 * This class provides database variables and methods for other classes.
 */
public class CassandraDao {
	
	// Transaction objects.
	private Cluster cluster = null;
	private Session session = null;
	private PreparedStatement prepared = null; 
	private BoundStatement bound = null;
	private ResultSet result = null;

	/*
	 * Initializes a JDBC connection and provides transaction objects.
	 */
	public CassandraDao() {
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("jing");
	}
	
	/*
	 *  Closes the session and the cluster.
	 */
	public void closeAll() {
		if (session != null)
			session.close();
		if (cluster != null)
			cluster.close();
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public PreparedStatement getPrepared() {
		return prepared;
	}

	public void setPrepared(PreparedStatement prepared) {
		this.prepared = prepared;
	}

	public BoundStatement getBound() {
		return bound;
	}

	public void setBound(BoundStatement bound) {
		this.bound = bound;
	}

	public ResultSet getResult() {
		return result;
	}

	public void setResult(ResultSet result) {
		this.result = result;
	}
}
