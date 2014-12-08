package es.uniovi.miw.monitora.agent;

import java.sql.Connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
		setStatus(Status.RUNNING);
		mainThread.start();
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
			try {
				mainThread.join();
			} catch (InterruptedException e) {
				logger.error("mainThread Interrupted exception", e);
			}
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
	boolean running;

	public MainThread() {
		super("Main Thread");
		dbm = new DBManager();
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

		testHibernate();

		Connection conn = dbm.getDBConn();

		running = true;

		while (running) {
			duStuff();
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

	private void testHibernate() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("monitora_ag");

		emf.close();

		logger.debug("--> Si no hay excepciones todo va bien");
		logger.debug("\n\t (O no hay ninguna clase mapeada)");
	}

	private void duStuff() {
		System.out.print(".");
		try {
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
		// tm.start();
	}

	private void closeServers() {
		logger.debug("clossing servers...");
		// tm.stop();
		dbm.stopDBServer();
	}
}
