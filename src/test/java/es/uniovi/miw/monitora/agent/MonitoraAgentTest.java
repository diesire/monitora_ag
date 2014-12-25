package es.uniovi.miw.monitora.agent;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.MonitoraAgent;
import es.uniovi.miw.monitora.agent.Status;

public class MonitoraAgentTest {

	static Logger logger = LoggerFactory.getLogger(App.class);
	private MonitoraAgent ag;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		ag = null;
	}

	@Test
	public void testGetStatus() {
		ag = new MonitoraAgent();
		logger.debug("monitoraAgent created");
		assertEquals(Status.CREATED, ag.getStatus());
		ag.start();
		logger.debug("monitoraAgent running");
		assertEquals(Status.RUNNING, ag.getStatus());
		ag.test();
		logger.debug("monitoraAgent test");
		assertEquals(Status.RUNNING, ag.getStatus());
		ag.stop();
		logger.debug("monitoraAgent stopped");
		assertEquals(Status.STOPPED, ag.getStatus());
		ag.exit();
		logger.debug("monitoraAgent terminated");
		assertEquals(Status.TERMINATED, ag.getStatus());
	}

}
