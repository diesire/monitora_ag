package es.uniovi.miw.monitora.agent;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uniovi.miw.monitora.agent.MonitoraAgent;
import es.uniovi.miw.monitora.agent.Status;

public class MonitoraAgentTest {

	private MonitoraAgent ag;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		ag = null;
	}

	@Test
	public void test() {
		ag = new MonitoraAgent();
		assertEquals(Status.CREATED, ag.getStatus());
		ag.start();
		assertEquals(Status.RUNNING, ag.getStatus());
		ag.test();
		assertEquals(Status.RUNNING, ag.getStatus());
		ag.stop();
		assertEquals(Status.STOPPED, ag.getStatus());
		ag.exit();
		assertEquals(Status.TERMINATED, ag.getStatus());
	}

}
