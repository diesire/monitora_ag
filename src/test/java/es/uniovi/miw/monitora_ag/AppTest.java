package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import es.uniovi.miw.monitora.core.model.Ack;
import es.uniovi.miw.monitora.core.model.Foo;

/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void testApp() {
		Client restClient = ClientBuilder.newClient();
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		WebTarget resourceTarget = target.path("c2/test");

		Foo foo = (Foo) resourceTarget.request()
				.header("Content-Type", MediaType.TEXT_XML).get(Foo.class);
		assertEquals("OK", foo.getBar());
	}
	
	public void testPing() {
		Client restClient = ClientBuilder.newClient();
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		WebTarget resourceTarget = target.path("c2/ping");

		Ack	ack = (Ack) resourceTarget.request()
				.header("Content-Type", MediaType.TEXT_XML).get(Ack.class);
		//assertEquals("OK", ack.getUpdate());
	}
}
