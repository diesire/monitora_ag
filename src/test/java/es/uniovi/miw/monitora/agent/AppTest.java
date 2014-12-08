package es.uniovi.miw.monitora.agent;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uniovi.miw.monitora.agent.App;
import es.uniovi.miw.monitora.agent.Status;

public class AppTest {

	App app;
	String[] args = { "App" };

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = Exception.class)
	public void test() throws Exception {
		app = new App(args);
		try {
			app.debug();
			assertTrue(true);

			app.help();
			assertTrue(true);

			app.exit();
			assertTrue(true);
		} catch (InterruptedException e) {
			fail();
		}

		throw new Exception("Exit can´t call System.exit()");
	}
}
