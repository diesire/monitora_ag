package es.uniovi.miw.monitora_ag;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		Client restClient = ClientBuilder.newClient();
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		WebTarget resourceTarget = target.path("c2/test");
		String responseString = (String) resourceTarget.request()
				.header("Content-Type", MediaType.TEXT_XML).get(String.class);
		System.out.println("Here is the response: " + responseString);
	}
}
