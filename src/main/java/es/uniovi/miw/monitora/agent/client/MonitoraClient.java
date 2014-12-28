package es.uniovi.miw.monitora.agent.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import es.uniovi.miw.monitora.agent.model.Agente;
import es.uniovi.miw.monitora.core.api.Ack;

public class MonitoraClient {

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

		try {

			Agente agent = target.path("c2/agent/" + agenteId).request()
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(Agente.class);

			return agent;
		} catch (Exception e) {

			// XXX handle ConnectException
			throw new Exception(e);
		}
	}
}
