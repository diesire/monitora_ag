package es.uniovi.miw.monitora.agent.client;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class MonitoraClient {

	static private Logger logger = LoggerFactory
			.getLogger(MonitoraClient.class);

	private WebTarget target;

	public MonitoraClient() {
		String uri = "http://localhost:8080/monitora_sv/rest/";
		logger.trace("Monitora client created. Uri: {}", uri);

		target = ClientBuilder.newClient().target(uri);
	}

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

	public void setSnapshot(int agenteId, Snapshot snapshot)
			throws BusinessException {
		Entity<Snapshot> entity = Entity.entity(snapshot,
				MediaType.APPLICATION_JSON_TYPE);
		Response response = target.path("c2/snapshot/" + agenteId).request()
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.post(entity);
		throw new BusinessException("Method not implemented");
	}

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

	public List<Agente> getAgentes() throws BusinessException {
		logger.trace("client.getAgentes()");
		try {
			List<Agente> agentes = target.path("c2/agentes").request()
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(new GenericType<List<Agente>>() {
					});

			return agentes;
		} catch (Exception e) {
			logger.debug("Client error thrown", e);
			throw new BusinessException(e);
		}
	}
}
