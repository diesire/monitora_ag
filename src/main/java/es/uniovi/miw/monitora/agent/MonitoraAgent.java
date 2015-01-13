package es.uniovi.miw.monitora.agent;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;

public class MonitoraAgent {
	public static final int CLIENT_ID = 1; // TODO: get from config file

	private static Logger logger = LoggerFactory.getLogger(MonitoraAgent.class);

	private Status status = Status.CREATED;

	private MonitoraClient client = new MonitoraClient();

	private void start() throws Exception {
		logger.debug("start");
		setStatus(Status.RUNNING);

		client.ping(CLIENT_ID);
		client.getAgente(CLIENT_ID);
	}

	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	private void stop() {
		logger.debug("stop");
		if (getStatus() == Status.RUNNING) {
			setStatus(Status.STOPPED);
		}
	}

	private void test() {
		try {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("monitora_ag");

			emf.close();
			logger.debug("Si no hay excepciones todo va bien o no hay ninguna clase mapeada)");
		} catch (Exception e) {
			logger.error("Error creando el contexto de persistencia", e);
		}
	}

	private void exit() {
		logger.debug("exit");
		if (getStatus() == Status.RUNNING) {
			stop();
		}
		setStatus(Status.TERMINATED);
	}

}
