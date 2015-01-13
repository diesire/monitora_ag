package es.uniovi.miw.monitora.agent.model;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.DBManager;
import es.uniovi.miw.monitora.agent.MonitoraAgent;
import es.uniovi.miw.monitora.server.model.Agente;
import es.uniovi.miw.monitora.server.model.Cliente;
import es.uniovi.miw.monitora.server.model.Consulta;
import es.uniovi.miw.monitora.server.model.Destino;
import es.uniovi.miw.monitora.server.model.InfPlanDest;
import es.uniovi.miw.monitora.server.model.Informe;
import es.uniovi.miw.monitora.server.model.InformeConsulta;
import es.uniovi.miw.monitora.server.model.InformeTipoDestino;
import es.uniovi.miw.monitora.server.model.LineaCron;
import es.uniovi.miw.monitora.server.model.Planificacion;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.TipoDestino;

public class PersistenceTest {

	static Logger logger = LoggerFactory.getLogger(PersistenceTest.class);
	private EntityManagerFactory factory;
	private Agente agente;
	private Cliente cliente;
	private Informe informePadre;
	private Informe informeHijo;
	private List<Object> graph;
	private Consulta consulta;
	private InformeConsulta informeConsulta;
	private TipoDestino tipoDestino;
	private InformeTipoDestino informeTipoDestino;
	private Destino destino;
	private Snapshot snapshot;
	private Date fecha;
	private Planificacion planificacion;
	private LineaCron linea1;
	private LineaCron linea2;
	private InfPlanDest infoPlanDest;
	private static MonitoraAgent monitoraAgent;
	private static DBManager dbm = new DBManager();

	@BeforeClass
	static public void setUpClass() {
		logger.debug("setUpClass");
		dbm.startDBServer();
	}

	@Before
	public void setUp() {
		logger.debug("setUp");
		factory = Persistence.createEntityManagerFactory("monitora_ag");
		graph = createGraph();
		persistGraph(graph);
	}

	@After
	public void tearDown() {
		logger.debug("tearDown");
		removeGraph();
		factory.close();
	}

	@AfterClass
	static public void tearDownClass() {
		logger.debug("tearDownClass");
//		monitoraAgent.exit();
//		monitoraAgent = null;
		dbm.closeDBServer();
	}

	@Test
	public void testAgente() {
		logger.debug("testAgente");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Agente ag = mapper.merge(agente);
		Cliente cl = mapper.merge(cliente);
		Destino des = mapper.merge(destino);

		assertNotNull(ag.getCliente());
		assertEquals(cl, ag.getCliente());
		assertTrue(ag.getDestinos().contains(des));

		trx.commit();
		mapper.close();
	}

	@Test
	public void testCliente() {
		logger.debug("testCliente");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Cliente cl = mapper.merge(cliente);
		Agente ag = mapper.merge(agente);

		assertEquals("Cliente", cl.getNombre());
		assertTrue(cl.getAgentes().contains(ag));

		trx.commit();
		mapper.close();
	}

	@Test
	public void testInforme() {
		logger.debug("testInforme");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Informe in1 = mapper.merge(informePadre);
		Informe in2 = mapper.merge(informeHijo);
		assertEquals("Nombre", in1.getNombre());
		assertTrue(in1.getContenidos().contains(in2));
		assertTrue(in2.getContenedores().contains(in1));
		assertEquals(fecha, in1.getFUltimaModificacion());
		assertEquals(fecha, in2.getFUltimaModificacion());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testConsulta() {
		logger.debug("testConsulta");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Consulta co = mapper.merge(consulta);
		TipoDestino tDes = mapper.merge(tipoDestino);

		assertEquals("S", co.getTipo());
		assertTrue(co.getTipoDestinos().contains(tDes));
		assertEquals(fecha, co.getFUltimaModificacion());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testInformeConsulta() {
		logger.debug("testInformeConsulta");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Informe in = mapper.merge(informePadre);
		Consulta co = mapper.merge(consulta);
		assertTrue(in.getInformeConsultas().containsAll(
				co.getInformeConsultas()));
		assertEquals(fecha, in.getFUltimaModificacion());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testTipoDestino() {
		logger.debug("testTipoDestino");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		TipoDestino tDes = mapper.merge(tipoDestino);
		Consulta co = mapper.merge(consulta);

		assertTrue(tDes.getConsultas().contains(co));
		assertTrue(co.getTipoDestinos().contains(tDes));

		trx.commit();
		mapper.close();
	}

	@Test
	public void testInformeTipoDestino() {
		logger.debug("testInformeTipoDestino");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		TipoDestino tDes = mapper.merge(tipoDestino);
		Informe co = mapper.merge(informePadre);

		assertTrue(tDes.getInformeTipoDestinos().containsAll(
				co.getInformeTipoDestinos()));

		trx.commit();
		mapper.close();
	}

	@Test
	public void testDestino() {
		logger.debug("testDestino");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Destino des = mapper.merge(destino);
		Cliente cli = mapper.merge(cliente);
		Agente ag = mapper.merge(agente);

		assertEquals(cli, des.getCliente());
		assertEquals(cli.getIdCliente(), des.getId().getIdCliente());
		assertTrue(des.getAgentes().contains(ag));

		trx.commit();
		mapper.close();
	}

	@Test
	public void testSnapshot() {
		logger.debug("testSnapshot");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Snapshot snap = mapper.merge(snapshot);
		Informe in = mapper.merge(informePadre);
		Destino des = mapper.merge(destino);

		assertEquals(in, snap.getInforme());
		assertEquals(des, snap.getDestino());
		assertEquals(fecha, snap.getFecha());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testPlanificacion() {
		logger.debug("testPlanificacion");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Planificacion plan = mapper.merge(planificacion);

		assertEquals(fecha, plan.getFUltimaModificacion());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testLineaCron() {
		logger.debug("testLineaCron");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		LineaCron l1 = mapper.merge(linea1);

		assertEquals("Descripcion", l1.getDescripcion());
		assertEquals(fecha, l1.getFUltimaModificacion());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testInfPlanDest() {
		logger.debug("testInfPlanDest");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		InfPlanDest inPD = mapper.merge(infoPlanDest);
		Informe in = mapper.merge(informePadre);
		Planificacion plan = mapper.merge(planificacion);
		Destino des = mapper.merge(destino);

		assertEquals(fecha, inPD.getFUltimaAplicacion());
		assertEquals(fecha, inPD.getFUltimaModificacion());
		assertEquals(in, inPD.getInforme());
		assertEquals(plan, inPD.getPlanificacion());
		assertEquals(des, inPD.getDestino());

		trx.commit();
		mapper.close();
	}

	private void persistGraph(List<Object> graph) {
		logger.debug("persistGraph {}", graph);
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : graph) {
			mapper.persist(o);
			logger.debug("\t + {}", o);
		}

		trx.commit();
		mapper.close();
	}

	private List<Object> createGraph() {
		logger.debug("createGraph");
		List<Object> res = new LinkedList<Object>();

		fecha = new Date(System.currentTimeMillis());

		informePadre = new Informe();
		informePadre.setNombre("Nombre");
		informePadre.setDescLarga("DescLarga");
		informePadre.setFUltimaModificacion(fecha);
		informePadre.setIdPlan(0);
		informePadre.setInfoId(0);

		informeHijo = new Informe();
		informeHijo.setNombre("Nombre");
		informeHijo.setDescLarga("DescLarga");
		informeHijo.setFUltimaModificacion(fecha);
		informeHijo.setIdPlan(0);
		informeHijo.setInfoId(1);

		consulta = new Consulta();
		consulta.setTipo("S");
		consulta.setDescCorta("DescCorta");
		consulta.setDescLarga("DescLarga");
		consulta.setFUltimaModificacion(fecha);

		informeConsulta = new InformeConsulta();
		informeConsulta.setFUltimaModificacion(fecha);

		informeTipoDestino = new InformeTipoDestino();
		informeTipoDestino.setPorDefecto("PorDefecto");

		tipoDestino = new TipoDestino();
		tipoDestino.setDescripcion("Descripción");
		tipoDestino.setNombreCorto("NombreCorto");

		cliente = new Cliente();
		cliente.setNombre("Cliente");

		agente = new Agente();
		agente.setComentarios("Comentarios");

		destino = new Destino();

		snapshot = new Snapshot();
		snapshot.setFecha(fecha);

		planificacion = new Planificacion();
		planificacion.setFUltimaModificacion(fecha);

		linea1 = new LineaCron();
		linea1.setDescripcion("Descripcion");
		linea1.setFUltimaModificacion(fecha);

		linea2 = new LineaCron();
		linea2.setDescripcion("Descripcion");
		linea2.setFUltimaModificacion(fecha);
		linea2.setIdLineaCron(1); // FIXME: auto id

		infoPlanDest = new InfPlanDest();
		infoPlanDest.setFUltimaAplicacion(fecha);
		infoPlanDest.setFUltimaModificacion(fecha);

		// link
		informePadre.addInfPlanDest(infoPlanDest);
		planificacion.addInfPlanDest(infoPlanDest);
		destino.addInfPlanDest(infoPlanDest);

		planificacion.addLineaCron(linea1);
		planificacion.addLineaCron(linea2);

		informePadre.addSnapshot(snapshot);
		destino.addSnapshot(snapshot);

		cliente.addAgente(agente);
		cliente.addDestino(destino);

		agente.addDestino(destino);
		destino.addAgente(agente);

		informePadre.addInformeConsulta(informeConsulta);
		consulta.addInformeConsulta(informeConsulta);

		consulta.addTipoDestino(tipoDestino);
		tipoDestino.addConsulta(consulta);

		informePadre.addInformeTipoDestino(informeTipoDestino);
		tipoDestino.addInformeTipoDestino(informeTipoDestino);

		informePadre.addContenido(informeHijo); // TODO add informe nesting

		res.add(cliente);
		res.add(agente);
		res.add(informeHijo);
		res.add(informePadre);
		res.add(consulta);
		res.add(tipoDestino);
		res.add(destino);
		res.add(snapshot);
		res.add(planificacion);
		res.add(linea1);
		res.add(linea2);
		res.add(infoPlanDest);

		logger.debug("\t -> {}", res);
		return res;
	}

	private List<Object> mergeGraph(EntityManager mapper) {
		logger.debug("mergeGraph (from mapper {})", mapper);
		List<Object> res = new LinkedList<Object>();
		Cliente cl = mapper.merge(cliente);
		Agente ag = mapper.merge(agente);
		Informe in1 = mapper.merge(informePadre);
		Informe in2 = mapper.merge(informeHijo);
		Consulta cons = mapper.merge(consulta);
		TipoDestino tDes = mapper.merge(tipoDestino);
		Destino des = mapper.merge(destino);
		Snapshot snap = mapper.merge(snapshot);
		Planificacion plan = mapper.merge(planificacion);
		LineaCron l1 = mapper.merge(linea1);
		LineaCron l2 = mapper.merge(linea2);
		InfPlanDest inPD = mapper.merge(infoPlanDest);

		res.add(cl);
		res.add(ag);
		res.add(in1);
		res.add(cons);
		res.add(tDes);
		res.add(in2);
		res.add(des);
		res.add(snap);
		res.add(plan);
		res.add(l1);
		res.add(l2);
		res.add(inPD);

		logger.debug("\t -> {}", res);
		return res;
	}

	private void removeGraph() {
		logger.debug("removeGraph");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : mergeGraph(mapper)) {
			mapper.remove(o);
			logger.debug("\t - {}", o);
		}

		trx.commit();
		mapper.close();
	}

}
