package es.uniovi.miw.monitora.agent.conf;

import static org.junit.Assert.*;

import org.junit.Test;

import es.uniovi.miw.monitora.server.conf.Conf;

public class ConfTest {

	@Test
	public void get() {
		assertEquals("monitoraag", Conf.get("dbname"));
	}

	@Test(expected = RuntimeException.class)
	public void getException() {
		Conf.get(null);
		fail("Must raise an exception");
	}

}
