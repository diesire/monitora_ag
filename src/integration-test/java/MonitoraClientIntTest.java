import static es.uniovi.miw.monitora.server.ui.util.Utils.AGENTE_ID_INVALID;
import static es.uniovi.miw.monitora.server.ui.util.Utils.IP_LOCAL;
import static es.uniovi.miw.monitora.server.ui.util.Utils.NOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.ui.util.TestUtils;

public class MonitoraClientIntTest {

	static Logger logger = LoggerFactory.getLogger(MonitoraClientIntTest.class);
	private MonitoraClient client = new MonitoraClient();
	private TestUtils testUtils = new TestUtils();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPing() throws Exception {
		Ack ack = client.ping(AGENTE_ID_INVALID);

		assertNotNull(ack.getUpdate().getTime());
		assertEquals(NOW.getDate(), ack.getUpdate().getTime().getDate());
	}

	@Test
	public void testPingException() throws BusinessException {
		assertNull(client.ping(AGENTE_ID_INVALID));
	}

	@Test
	public void testAgente() throws Exception {
		Agente agente = client.getAgente(50);
		assertEquals(IP_LOCAL, agente.getIpAgente());
		assertEquals("ssss", agente.getComentarios());
	}

	@Test
	public void testAgenteException() throws BusinessException {
		assertNull(client.getAgente(AGENTE_ID_INVALID));
	}

	public void testAgentes() throws BusinessException {
		for (Agente agente : client.getAgentes()) {
			testUtils.testHierarchy(agente);
		}
	}
}
