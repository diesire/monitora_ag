package es.uniovi.miw.monitora.agent.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.core.Status;

public class DBManager {

	private Logger logger = LoggerFactory.getLogger(DBManager.class);
	// TODO: change it to your db location
	private final String dbLocation = "data/";
	private Server server;
	private Connection dbConn = null;
	private Status status = Status.CREATED;

	public void startDBServer() throws Exception {

		logger.debug("startDBServer");

		HsqlProperties props = new HsqlProperties();
		props.setProperty("server.database.0", "file:" + dbLocation + "mydb;");
		props.setProperty("server.dbname.0", "monitoraag");
		props.setProperty("server.port", 9002);
		server = new org.hsqldb.Server();

		try {
			server.setProperties(props);
			server.start();
			setStatus(Status.RUNNING);
		} catch (Exception e) {
			logger.error("Error starting DB server", e);
			throw new Exception(e);
		}
	}

	public void stopDBServer() {
		if (status == Status.RUNNING) {
			logger.debug("stopDBServer");
			server.stop();
			setStatus(Status.STOPPED);
		}
	}

	public Connection getDBConn() throws Exception {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			dbConn = DriverManager.getConnection(
					"jdbc:hsqldb:hsql://localhost/monitoraag", "SA", "");
		} catch (Exception e) {
			logger.error("Error getting DB connection", e);
			throw new Exception(e);
		}

		return dbConn;
	}

	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	public void closeDBServer() {
		if (getStatus() == Status.RUNNING) {
			stopDBServer();
		}

		server.shutdown();
		setStatus(Status.TERMINATED);
	}
}