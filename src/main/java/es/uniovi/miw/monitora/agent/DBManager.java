package es.uniovi.miw.monitora.agent;

import java.sql.Connection;
import java.sql.DriverManager;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {
	Logger logger = LoggerFactory.getLogger(DBManager.class);

	final String dbLocation = "data/"; // change it to your db location
	Server server;
	Connection dbConn = null;

	public void startDBServer() {
		logger.debug("startDBServer");

		HsqlProperties props = new HsqlProperties();
		props.setProperty("server.database.0", "file:" + dbLocation + "mydb;");
		props.setProperty("server.dbname.0", "monitoraag");
		server = new org.hsqldb.Server();
		try {
			server.setProperties(props);
		} catch (Exception e) {
			return;
		}
		server.start();
	}

	public void stopDBServer() {
		logger.debug("stopDBServer");
		server.stop();
		server.shutdown();
	}

	public Connection getDBConn() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			dbConn = DriverManager.getConnection(
					"jdbc:hsqldb:hsql://localhost/monitoraag", "SA", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbConn;
	}
}