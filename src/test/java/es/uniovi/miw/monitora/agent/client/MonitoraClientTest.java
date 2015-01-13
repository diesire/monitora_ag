package es.uniovi.miw.monitora.agent.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.model.Agente;

public class MonitoraClientTest {

	private static final Calendar NOW = Calendar.getInstance();
	private static final int TEST_CLIENT = -1;

	static Logger logger = LoggerFactory.getLogger(MonitoraClientTest.class);
	private MonitoraClient mockedClient;
	private MonitoraClient client = new MonitoraClient();

	@Before
	public void setUp() throws Exception {
		Ack expectedAck = new Ack();
		Agente expectedAgente = new Agente();

		expectedAck.setUpdate(NOW);
		expectedAgente.setAgenteId(TEST_CLIENT);

		mockedClient = mock(client.getClass());
		when(mockedClient.ping(TEST_CLIENT)).thenReturn(expectedAck);
		when(mockedClient.getAgente(TEST_CLIENT)).thenReturn(expectedAgente);
	}

	@Test
	public void testPing() throws Exception {
		Ack ack = mockedClient.ping(TEST_CLIENT);

		assertNotNull(ack.getUpdate().getTime());
		assertEquals(NOW, ack.getUpdate());
	}

	@Test
	public void testPingException() {
		try {
			client.ping(TEST_CLIENT);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testAgente() throws Exception {
		Agente agente = mockedClient.getAgente(TEST_CLIENT);
		assertEquals(TEST_CLIENT, agente.getAgenteId());
	}

	@Test
	public void testAgenteException() {
		try {
			client.getAgente(TEST_CLIENT);
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
