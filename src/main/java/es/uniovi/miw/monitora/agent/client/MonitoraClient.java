package es.uniovi.miw.monitora.agent.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.core.api.Task;

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

	public List<Task> tasks() {
		List<Task> tasks = target.path("c2/tasks/" + clientId).request()
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Task>>() {
				});

		return tasks;
	}
}
