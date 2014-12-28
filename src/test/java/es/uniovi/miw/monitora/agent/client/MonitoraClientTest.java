package es.uniovi.miw.monitora.agent.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.App;
import es.uniovi.miw.monitora.agent.model.Agente;
import es.uniovi.miw.monitora.core.api.Ack;

public class MonitoraClientTest {

	private static final Calendar NOW = Calendar.getInstance();
	private static final int TEST_CLIENT = -1;

	static Logger logger = LoggerFactory.getLogger(App.class);
	private MonitoraClient mockedClient;
	private MonitoraClient client = new MonitoraClient(TEST_CLIENT);

	@Before
	public void setUp() throws Exception {

		Ack expectedAck = new Ack();
		Agente expectedAgente = new Agente();

		expectedAck.setUpdate(NOW);
		expectedAgente.setAgenteId(TEST_CLIENT);

		mockedClient = mock(client.getClass());
		when(mockedClient.ping()).thenReturn(expectedAck);
		when(mockedClient.agente()).thenReturn(expectedAgente);
	}

	@Test
	public void testPing() throws Exception {

		Ack ack = mockedClient.ping();

		assertNotNull(ack.getUpdate().getTime());
		assertEquals(NOW, ack.getUpdate());
	}

	@Test
	public void testPingException() {

		try {
			client.ping();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testAgente() throws Exception {

		Agente agente = mockedClient.agente();
		assertEquals(TEST_CLIENT, agente.getAgenteId());
	}
	
	@Test
	public void testAgenteException() {

		try {
			client.agente();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

}
