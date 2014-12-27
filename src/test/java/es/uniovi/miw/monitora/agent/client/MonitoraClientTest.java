package es.uniovi.miw.monitora.agent.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.App;
import es.uniovi.miw.monitora.core.api.Ack;

public class MonitoraClientTest {

	private static final Calendar NOW = Calendar.getInstance();
	private static final String TEST_CLIENT = "AppTestClient";

	static Logger logger = LoggerFactory.getLogger(App.class);
	private MonitoraClient mockedClient;
	private MonitoraClient client = new MonitoraClient(TEST_CLIENT);

	@Before
	public void setUp() throws Exception {
		
		Ack expectedAck = new Ack();

		expectedAck.setUpdate(NOW);

		mockedClient = mock(client.getClass());
		when(mockedClient.ping()).thenReturn(expectedAck);
	}

	@Test
	public void testPing() throws Exception {

		Ack ack = mockedClient.ping();

		assertNotNull(ack.getUpdate().getTime());
		assertEquals(NOW, ack.getUpdate());
	}

	@Test(expected = Exception.class)
	public void testPingException() throws Exception {

		client.ping();
		fail();
	}

}
