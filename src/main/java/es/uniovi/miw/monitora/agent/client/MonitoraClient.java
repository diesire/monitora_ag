package es.uniovi.miw.monitora.agent.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uniovi.miw.monitora.core.api.Ack;

public class MonitoraClient {

	private String clientId;
	private WebTarget target;

	public MonitoraClient(String clientId) {
		this.clientId = clientId;
		Client restClient = ClientBuilder.newClient();
		target = restClient.target("http://localhost:8080/monitora_sv/rest/");
	}

	public Ack ping() {

		Ack ack = target.path("c2/ping/" + clientId).request()
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.get(Ack.class);

		return ack;
	}
}
