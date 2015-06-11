package es.uniovi.miw.monitora.agent.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.agent.persistence.DBManager;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.core.impl.agente.FindAgenteById;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;

public class MonitoraAgent implements IMonitoraAgent {
	public static final int CLIENT_ID = -1; // TODO: get from config file

	private static Logger logger = LoggerFactory.getLogger(MonitoraAgent.class);

	private Status status = Status.CREATED;
	private MonitoraClient client = new MonitoraClient();
	// private DBManager dbm = new DBManager();
	private PersistenceService db = PersistenceFactory.getPersistenceService();

	public void start() throws Exception {
		logger.debug("start");
		try {
			// dbm.startDBServer();
			db.start();
			setStatus(Status.RUNNING);

			List<Agente> agentes;
			agentes = client.getAgentes();

			Ack ack = client.ping(100);
			if (ack != null) {
				Agente agente = client.getAgente(100);
			}

			// Agente agente = client.getAgente(CLIENT_ID);
			//
			// if (agente != null) {
			// if (ServicesFactory.getAgenteService().findAgenteById(-1) !=
			// null) {
			// logger.warn("Agente already in DB");
			// }
			// ServicesFactory.getAgenteService().updateAgente(agente);
			// }
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

	public void stop() throws BusinessException {
		logger.debug("stop");
		if (getStatus() == Status.RUNNING) {
			// dbm.stopDBServer();
			db.stop();
			setStatus(Status.STOPPED);
		}
	}

	public void exit() throws BusinessException {
		logger.debug("exit");
		if (getStatus() == Status.RUNNING) {
			stop();
		}

//		dbm.closeDBServer();
		setStatus(Status.TERMINATED);
	}

}
