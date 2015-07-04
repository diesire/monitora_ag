package es.uniovi.miw.monitora.agent.snapshot;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.Consulta;
import es.uniovi.miw.monitora.server.model.Destino;
import es.uniovi.miw.monitora.server.model.InfPlanDest;
import es.uniovi.miw.monitora.server.model.Informe;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.ui.util.TestUtils;

public class SnapshotManagerTest {

	private TestUtils testUtils = new TestUtils();;

	@BeforeClass
	public static void setUpClass() throws Exception {
		PersistenceFactory.getPersistenceService().start();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		PersistenceFactory.getPersistenceService().stop();
	}

	@Test
	public void testCreateSnapshot() throws BusinessException {

		Agente ag = testUtils.createHierarchy();
		Destino des = ag.getDestinos().iterator().next();
		InfPlanDest infoPlanDes = des.getInfPlanDests().iterator().next();
		Informe info = infoPlanDes.getInforme();
		Consulta con = info.getInformeConsultas().iterator().next()
				.getConsulta();
		SnapshotManager snapManager = SnapshotManager.getInstance();

		Snapshot snap = snapManager.createSnapshot(des.getId().getIdCliente(),
				des.getId().getIdDestino(), info.getInfoId());

		//Getting back again the entities fixes Date issue
		Destino foundDes = ServicesFactory.getDestinoService().findDestinoById(
				des.getId().getIdDestino(), des.getId().getIdCliente());
		Informe foundInf = ServicesFactory.getInformeService().findInformeById(
				info.getInfoId());

		Snapshot snapFound = ServicesFactory.getSnapshotService()
				.findSnapshotById(snap.getId().getIdSnapshot(),
						snap.getId().getIdDestino(),
						snap.getId().getIdCliente());

		testUtils.testLink(snapFound, foundDes, foundInf);
	}

	@Test
	public void testDumpToXml() throws BusinessException {
		Agente ag = testUtils.createHierarchy();
		Destino des = ag.getDestinos().iterator().next();
		InfPlanDest infoPlanDes = des.getInfPlanDests().iterator().next();
		Informe info = infoPlanDes.getInforme();
		Consulta con = info.getInformeConsultas().iterator().next()
				.getConsulta();
		SnapshotManager snapManager = SnapshotManager.getInstance();

		Snapshot snap = snapManager.createSnapshot(des.getId().getIdCliente(),
				des.getId().getIdDestino(), info.getInfoId());

		URI uri = snapManager.dumpSnapshot(snap, "CLIENTE");

		assertTrue(Files.isReadable(Paths.get(uri)));
	}
}
