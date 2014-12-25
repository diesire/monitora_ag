package es.uniovi.miw.monitora.agent;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		DBManager dbm = new DBManager();
		assertEquals(Status.CREATED, dbm.getStatus());

		dbm.startDBServer();
		assertEquals(Status.RUNNING, dbm.getStatus());

		dbm.stopDBServer();
		assertEquals(Status.STOPPED, dbm.getStatus());

		dbm.startDBServer();
		assertEquals(Status.RUNNING, dbm.getStatus());

		dbm.stopDBServer();
		assertEquals(Status.STOPPED, dbm.getStatus());

		dbm.closeDBServer();
		assertEquals(Status.TERMINATED, dbm.getStatus());

	}

}
