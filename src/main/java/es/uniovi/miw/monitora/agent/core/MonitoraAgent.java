package es.uniovi.miw.monitora.agent.core;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.*;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.agent.snapshot.ScpUtils;
import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.agent.task.quartz.ConsultaJob;
import es.uniovi.miw.monitora.agent.task.quartz.SnapshotJobListener;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.conf.Conf;
import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.Destino;
import es.uniovi.miw.monitora.server.model.InfPlanDest;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;

public class MonitoraAgent implements IMonitoraAgent {
	private static Logger logger = LoggerFactory.getLogger(MonitoraAgent.class);

	private Status status = Status.CREATED;
	private MonitoraClient client = new MonitoraClient();
	private PersistenceService db = PersistenceFactory.getPersistenceService();
	private SchedulerService schedulerService;
	private SnapshotManager snapManager = SnapshotManager.getInstance();
	private Integer agenteId;

	private Ack ack = null;
	private Agente agente = null;
	private Map<Snapshot, String> dumps = new HashMap<Snapshot, String>();

	public MonitoraAgent() throws BusinessException {
		schedulerService = AgentServiceFactory.getSchedulerService();
		agenteId = new Integer(Conf.get("agente.id"));
	}

	public void start() throws Exception {
		logger.debug("start");
		try {
			db.start();
			setStatus(Status.RUNNING);
			schedulerService.start();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception(e);
		}
	}

	public void sendResults() throws BusinessException {
		dumps = snapManager.dump();
		for (Entry<Snapshot, String> entry : dumps.entrySet()) {
			try {
				Path base = Paths.get(Conf.get("snapshot.path"));
				Path zFile = Paths.get(entry.getValue());
				Path relative = base.relativize(zFile);
				Path remoteZFile = Paths.get(Conf.get("server.snapshot.path"))
						.resolve(relative);

				ScpUtils.copyTo(Conf.get("ssh.user"), Conf.get("ssh.password"),
						Conf.get("ssh.host"),
						new Integer(Conf.get("ssh.port")).intValue(),
						remoteZFile.toString(), entry.getValue());

				client.setSnapshot(agenteId, entry.getKey());

				// TODO if send OK, delete file and entry

			} catch (Exception e) {
				// try next entry
			}
		}
	}

	public void updateTasks() throws BusinessException {
		ack = ping();

		if (ack != null) {
			updateAgent();
		}

		agente = getAgente();
		for (Destino destino : agente.getDestinos()) {
			addTask(destino);
		}
	}

	private void addTask(Destino destino) throws BusinessException {
		for (InfPlanDest infoPlanDes : destino.getInfPlanDests()) {
			schedulerService.add(infoPlanDes);
		}
	}

	private Agente getAgente() throws BusinessException {
		Agente ag;
		try {
			ag = ServicesFactory.getAgenteService().findAgenteById(agenteId);
		} catch (BusinessException e) {
			logger.error("Agent {} not found", agenteId);
			throw new BusinessException(MessageFormat.format(
					"Agent {0} not found in agent", agenteId));
		}
		return ag;
	}

	private void updateAgent() throws BusinessException {
		try {
			Agente updateAgente = client.getAgente(agenteId);
			logger.info("Agent {} retrieved", agenteId);
			if (updateAgente == null) {
				throw new BusinessException("Agent retrieved is empty");
			}
			ServicesFactory.getAgenteService().updateAgente(updateAgente);
			logger.info("Agente {} saved", agenteId);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	private Ack ping() {
		Ack ack = null;
		try {
			ack = client.ping(agenteId);
			logger.info("Ping OK");
		} catch (Exception e) {
			logger.warn("Ping failed");
			logger.debug(e.getLocalizedMessage());
		}
		return ack;
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
			schedulerService.stop();
			db.stop();
			setStatus(Status.STOPPED);
		}
	}

	public void exit() throws BusinessException {
		logger.debug("exit");
		if (getStatus() == Status.RUNNING) {
			stop();
		}

		// dbm.closeDBServer();
		setStatus(Status.TERMINATED);
	}

}
