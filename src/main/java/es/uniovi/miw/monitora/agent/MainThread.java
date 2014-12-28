package es.uniovi.miw.monitora.agent;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.agent.model.Agente;

public class MainThread extends Thread {
	public Logger logger = LoggerFactory.getLogger(InteractiveThread.class);
	private DBManager dbm;
	// private TaskManager tm;
	private boolean running;
	private MonitoraClient monitoraClient;

	public MainThread() {
		super("Main Thread");
		dbm = new DBManager();
		assert dbm.getStatus() == Status.CREATED;
		monitoraClient = new MonitoraClient(MonitoraAgent.CLIENT_ID);
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
			try {

				monitoraClient.ping();
				Agente agente = monitoraClient.agente();

				// meter el informe en persistencia
				// actualizar TaskManager
				// run taskManager

			} catch (Exception e) {
				logger.error("Error in MonitoraClient", e);
			}
		}

		while (running) {
			logger.debug("main thread running...");
			try {
				Thread.currentThread();
				Thread.sleep(10000);
			} catch (InterruptedException ie) {
				logger.debug("main thread interrupted exception", ie);
				running = false;
			}
		}

		logger.debug("closig...");
		closeServers();
	}

	private void startServers() {
		logger.debug("starting servers...");
		dbm.startDBServer();
		assert dbm.getStatus() == Status.RUNNING;
		// tm.start();
	}

	private void closeServers() {
		logger.debug("closing servers...");
		// tm.stop();
		dbm.stopDBServer();
		assert dbm.getStatus() == Status.STOPPED;
		dbm.closeDBServer();
		assert dbm.getStatus() == Status.TERMINATED;
	}
}
