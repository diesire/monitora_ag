package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.model.Agente;
import es.uniovi.miw.monitora.agent.model.Cliente;
import es.uniovi.miw.monitora.agent.model.Consulta;
import es.uniovi.miw.monitora.agent.model.Destino;
import es.uniovi.miw.monitora.agent.model.Informe;
import es.uniovi.miw.monitora.agent.model.InformeConsulta;
import es.uniovi.miw.monitora.agent.model.InformeTipoDestino;
import es.uniovi.miw.monitora.agent.model.TipoDestino;

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

	public void testAgente() {
		logger.debug("testAgente");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Agente ag = mapper.merge(agente);
		Cliente cl = mapper.merge(cliente);

		assertNotNull(ag.getCliente());
		assertEquals(cl, ag.getCliente());

		trx.commit();
		mapper.close();
	}

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
		assertEquals("S", co.getTipo());

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

		assertEquals(cli, des.getCliente());
		assertEquals(cli.getIdCliente(), des.getId().getIdCliente());

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

		informePadre = new Informe();
		informePadre.setNombre("Nombre");
		informePadre.setDescLarga("DescLarga");
		informePadre
				.setFUltimaModificacion(new Date(System.currentTimeMillis()));
		informePadre.setIdPlan(0);
		informePadre.setInfoId(0);

		informeHijo = new Informe();
		informeHijo.setNombre("Nombre");
		informeHijo.setDescLarga("DescLarga");
		informeHijo
				.setFUltimaModificacion(new Date(System.currentTimeMillis()));
		informeHijo.setIdPlan(0);
		informeHijo.setInfoId(1);

		consulta = new Consulta();
		consulta.setTipo("S");
		consulta.setDescCorta("DescCorta");
		consulta.setDescLarga("DescLarga");
		consulta.setFUltimaModificacion(new Date(System.currentTimeMillis()));

		informeConsulta = new InformeConsulta();
		informeConsulta.setFUltimaModificacion(new Date(System
				.currentTimeMillis()));

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

		// link
		cliente.addAgente(agente);

		cliente.addDestino(destino);

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

		res.add(cl);
		res.add(ag);
		res.add(in1);
		res.add(cons);
		res.add(tDes);
		res.add(in2);
		res.add(des);

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
