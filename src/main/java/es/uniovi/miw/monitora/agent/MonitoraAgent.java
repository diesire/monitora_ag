package es.uniovi.miw.monitora.agent;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.annotations.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoraAgent {
	private static Logger logger = LoggerFactory.getLogger(MonitoraAgent.class);

	private Status status = Status.CREATED;

	private MainThread mainThread;

	public MonitoraAgent() {
		mainThread = new MainThread();
	}

	public void start() {
		logger.debug("start");
		mainThread.start();
		setStatus(Status.RUNNING);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void stop() {
		logger.debug("stop");
		if (getStatus() == Status.RUNNING) {
			setStatus(Status.STOPPED);
			mainThread.termitate();
			// try {
			// mainThread.join();
			// } catch (InterruptedException e) {
			// logger.error("mainThread Interrupted exception", e);
			// }
		}
	}

	public void test() {
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("monitora_ag");

			emf.close();
			logger.debug("Si no hay excepciones todo va bien o no hay ninguna clase mapeada)");
		} catch (Exception e) {
			logger.error("Error creando el contexto de persistencia", e);
		}
	}

	public void exit() {
		logger.debug("exit");
		if (getStatus() == Status.RUNNING) {
			stop();
		}
		setStatus(Status.TERMINATED);
	}

}

class MainThread extends Thread {
	public Logger logger = LoggerFactory.getLogger(InteractiveThread.class);
	private DBManager dbm;
	// private TaskManager tm;
	private boolean running;

	public MainThread() {
		super("Main Thread");
		dbm = new DBManager();
		assert dbm.getStatus() == Status.CREATED;
		// tm = new QuartzTaskManager();
	}

	public void termitate() {
		logger.debug("terminate");
		running = false;
	}

	@Override
	public void run() {
		logger.debug("running...");
		startServers();

		Connection conn = dbm.getDBConn();

		if (conn == null) {

			logger.error("Connection refused");
			running = false;
		} else {

			running = true;
		}

		while (running) {
			doStuff();
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				logger.debug("main thread interrupted exception", ie);
				running = false;
			}
		}

		logger.debug("clossig...");
		closeServers();
	}

	private void doStuff() {
		System.out.print(".");
		try {
			//
			//
			// // Some examples
			// Statement stmt = conn.createStatement();
			// stmt.executeQuery("CREATE TABLE IF NOT EXISTS answers (num INT IDENTITY, answer VARCHAR(250))");
			// stmt.executeQuery("INSERT INTO answers (answer) values ('this is a new answer')");
			// ResultSet rs =
			// stmt.executeQuery("SELECT num, answer FROM answers");
			// while (rs.next()) {
			// System.out.println("Answer number: " + rs.getString("num")
			// + "; answer text: " + rs.getString("answer"));
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startServers() {
		logger.debug("starting servers...");
		dbm.startDBServer();
		assert dbm.getStatus() == Status.RUNNING;
		// tm.start();
	}

	private void closeServers() {
		logger.debug("clossing servers...");
		// tm.stop();
		dbm.stopDBServer();
		assert dbm.getStatus() == Status.STOPPED;
		dbm.closeDBServer();
		assert dbm.getStatus() == Status.TERMINATED;
	}
}
