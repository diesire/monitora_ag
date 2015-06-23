package es.uniovi.miw.monitora.agent.snapshot;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class ScpUtilsTest {

	private static final String PASSWORD = "bbb";
	private static final String USER = "aaa";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws BusinessException {
		ScpUtils.copyTo(USER, PASSWORD, "127.0.0.1", 22,
				"/Users/diesire/tmp/server/snapshot-950-35-68.zip",
				"/Users/diesire/tmp/snapshot-950-35-68.zip");

		assertTrue(Files.isReadable(Paths
				.get("/Users/diesire/tmp/server/snapshot-950-35-68.zip")));
	}

}
