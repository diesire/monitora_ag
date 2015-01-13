package es.uniovi.miw.monitora.agent.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.model.Agente;

public class MonitoraClient {

	static private Logger logger = LoggerFactory
			.getLogger(MonitoraClient.class);

	private int agenteId;
	private WebTarget target;

	public MonitoraClient(int agenteId) {

		this.agenteId = agenteId;
		Client restClient = ClientBuilder.newClient();
		target = restClient.target("http://localhost:8080/monitora_sv/rest/");
	}

	public Ack ping() throws Exception {

		try {

			Ack ack = target.path("c2/ping/" + agenteId).request()
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(Ack.class);

			return ack;
		} catch (Exception e) {

			// XXX handle ConnectException
			throw new Exception(e);
		}
	}

	public Agente agente() throws Exception {

		// try {
		//
		// Agente agent = target.path("c2/agente/" + agenteId).request()
		// .header("Content-Type", MediaType.APPLICATION_JSON)
		// .get(Agente.class);
		//
		// return agent;
		// } catch (Exception e) {
		//
		// // XXX handle ConnectException
		// throw new Exception(e);
		// }

		logger.trace("Agente[{}]", agenteId);
		Response response = target.path("c2/agente/" + agenteId)
				.request(MediaType.APPLICATION_JSON_TYPE).get();

			Agente agente = response.readEntity(Agente.class);
			return agente;
	}

	public static void main(String[] args) throws Exception {
//			Ack ack = new MonitoraClient(-1).ping();
			Agente agente = new MonitoraClient(-1).agente();
	}
}
