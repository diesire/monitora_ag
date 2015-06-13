import static es.uniovi.miw.monitora.server.ui.util.TestUtils.AGENTE_ID_INVALID;
import static es.uniovi.miw.monitora.server.ui.util.TestUtils.NOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;
import es.uniovi.miw.monitora.server.ui.util.TestUtils;

public class MonitoraClientIntTest {

	static Logger logger = LoggerFactory.getLogger(MonitoraClientIntTest.class);
	private MonitoraClient client = new MonitoraClient();
	private TestUtils testUtils = new TestUtils();
	private static PersistenceService db;

	@BeforeClass
	static public void setUpClass() throws Exception {
		db = PersistenceFactory.getPersistenceService();
		db.start();
	}

	@AfterClass
	public static void tearDownClass() throws BusinessException {
		db.stop();
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
		Agente agente = client.getAgentes().iterator().next();

		testUtils.testHierarchy(agente);
		ServicesFactory.getAgenteService().updateAgente(agente);

		Agente found = ServicesFactory.getAgenteService().findAgenteById(
				agente.getAgenteId());

		testUtils.testHierarchy(found);
	}

	@Test
	public void testAgenteNull() throws BusinessException {
		assertNull(client.getAgente(AGENTE_ID_INVALID));
	}

	@Test
	public void testAgentes() throws BusinessException {
		for (Agente agente : client.getAgentes()) {
			testUtils.testHierarchy(agente);
		}
	}
}
