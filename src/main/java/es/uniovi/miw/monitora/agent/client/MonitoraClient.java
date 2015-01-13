package es.uniovi.miw.monitora.agent.client;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.core.snapshot.Snapshot;
import es.uniovi.miw.monitora.server.core.IMonitoraServer;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class MonitoraClient implements IMonitoraServer {

	static private Logger logger = LoggerFactory
			.getLogger(MonitoraClient.class);

	private WebTarget target;

	public MonitoraClient() {
		String uri = "http://localhost:8080/monitora_sv/rest/";
		logger.trace("Monitora client created. Uri: {}", uri);

		target = ClientBuilder.newClient().target(uri);
	}

	@Override
	public Ack ping(int agenteId) throws BusinessException {
		try {
			logger.trace("client.ping({})", agenteId);
			Ack ack = target.path("c2/ping/" + agenteId).request()
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(Ack.class);

			return ack;
		} catch (Exception e) {
			logger.debug("Client error thrown", e);
			throw new BusinessException(e);
		}
	}

	@Override
	public void setSnapshot(int agenteId, Snapshot snapshot)
			throws BusinessException {
		// TODO Auto-generated method stub
		throw new BusinessException("Method not implemented");
	}

	@Override
	public Agente getAgente(int agenteId) throws BusinessException {
		logger.trace("client.getAgente({})", agenteId);
		try {
			Agente agent = target.path("c2/agente/" + agenteId).request()
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(Agente.class);

			return agent;
		} catch (Exception e) {
			logger.debug("Client error thrown", e);
			throw new BusinessException(e);
		}
	}
}
