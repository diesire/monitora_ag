package es.uniovi.miw.monitora_ag;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.model.Ack;

/**
 * Unit test for simple App.
 */
public class AppTest {
	static Logger logger = LoggerFactory.getLogger(App.class);
	
	@Test
	public void testPing() {
		Client restClient = ClientBuilder.newClient();
		logger.debug("Ping");
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		String clientId = "AppTestClient";
		WebTarget resourceTarget = target.path("c2/ping/" + clientId);

		Ack ack = (Ack) resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON).get(Ack.class);
		logger.debug("ack {}", ack.getUpdate().getTime());
		// assertEquals("OK", ack.getUpdate());
	}
}
