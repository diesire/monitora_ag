package es.uniovi.miw.monitora.agent.snapshot;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.stream.FileImageOutputStream;

import org.hibernate.type.AssociationType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class ZipUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws BusinessException {
		Path dir = Paths.get("/Users/diesire/tmp/snapshot-950-35-68");
		Path zfile = dir.getParent().resolve("snapshot-950-35-68.zip");
		try {
			FileOutputStream os = new FileOutputStream(zfile.toFile());
			ZipUtils.zipFolder(dir.toFile(), os);
			os.close();
		} catch (IOException e) {
			throw new BusinessException(e);
		}

		assertTrue(Files.isReadable(zfile));
	}

}
