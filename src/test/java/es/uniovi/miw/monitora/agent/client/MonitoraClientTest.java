package es.uniovi.miw.monitora.agent.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class MonitoraClientTest {

	private static final String COMENTARIO = "Expected agente";
	private static final String LOCAL_IP = "127.0.01";
	private static final Calendar NOW = Calendar.getInstance();
	private static final int TEST_CLIENT = -1;

	static Logger logger = LoggerFactory.getLogger(MonitoraClientTest.class);
	private MonitoraClient mockedClient;
	private MonitoraClient client = new MonitoraClient();

	@Before
	public void setUp() throws Exception {
		Ack expectedAck = new Ack();
		Agente expectedAgente = new Agente();
		expectedAgente.setIpAgente(LOCAL_IP);
		expectedAgente.setComentarios(COMENTARIO);

		expectedAck.setUpdate(NOW);

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

	@Test(expected = BusinessException.class)
	public void testPingException() throws BusinessException {
		client.ping(TEST_CLIENT);
	}

	@Test
	public void testAgente() throws Exception {
		Agente agente = mockedClient.getAgente(TEST_CLIENT);
		assertEquals(LOCAL_IP, agente.getIpAgente());
		assertEquals(COMENTARIO, agente.getComentarios());
	}

	@Test(expected = BusinessException.class)
	public void testAgenteException() throws BusinessException {
		client.getAgente(TEST_CLIENT);
	}
}
