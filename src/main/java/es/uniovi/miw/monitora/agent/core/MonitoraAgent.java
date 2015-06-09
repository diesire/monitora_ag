package es.uniovi.miw.monitora.agent.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.agent.persistence.DBManager;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.core.impl.agente.FindAgenteById;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class MonitoraAgent implements IMonitoraAgent {
	public static final int CLIENT_ID = -1; // TODO: get from config file

	private static Logger logger = LoggerFactory.getLogger(MonitoraAgent.class);

	private Status status = Status.CREATED;
	private MonitoraClient client = new MonitoraClient();
	private DBManager dbm = new DBManager();

	public void start() throws Exception {
		logger.debug("start");
		try {
			dbm.startDBServer();
			setStatus(Status.RUNNING);

			client.ping(CLIENT_ID);
			Agente agente = client.getAgente(CLIENT_ID);

			if (agente != null) {
				if (ServicesFactory.getAgenteService().findAgenteById(-1) != null) {
					logger.warn("Agente already in DB");
				}
				ServicesFactory.getAgenteService().updateAgente(agente);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception(e);
		}
	}

	public Status getStatus() {
		return status;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	public void stop() {
		logger.debug("stop");
		if (getStatus() == Status.RUNNING) {
			dbm.stopDBServer();
			setStatus(Status.STOPPED);
		}
	}

	public void exit() {
		logger.debug("exit");
		if (getStatus() == Status.RUNNING) {
			stop();
		}

		dbm.closeDBServer();
		setStatus(Status.TERMINATED);
	}

}
