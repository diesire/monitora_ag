package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class PersistenceTest {

	static Logger logger = LoggerFactory.getLogger(PersistenceTest.class);
	private EntityManagerFactory factory;
	private Agente agente;
	private Cliente cliente;
	private List<Object> graph;

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
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Agente ag = (Agente) mapper.merge(agente);

		// asserts
		assertNotNull(ag.getDestinos());

		trx.commit();
		mapper.close();
	}

	@Test
	public void testCliente() {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Cliente cl = (Cliente) mapper.merge(cliente);

		assertEquals("Cliente", cl.getNombre());

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
		cliente = new Cliente();
		cliente.setNombre("Cliente");

		res.add(cliente);

		logger.debug("\t -> {}", res);
		return res;
	}

	private List<Object> mergeGraph(EntityManager mapper) {
		logger.debug("mergeGraph (from mapper {}), mapper");
		List<Object> res = new LinkedList<Object>();
		Cliente cl = mapper.merge(cliente);

		res.add(cl);

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
