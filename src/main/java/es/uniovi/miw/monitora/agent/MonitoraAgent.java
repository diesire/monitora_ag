package es.uniovi.miw.monitora.agent;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoraAgent {
	public static final int CLIENT_ID = 1; //TODO: get from config file

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
