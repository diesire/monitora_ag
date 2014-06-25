package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.*;

import es.uniovi.miw.monitora.agent.model.Agente;
import es.uniovi.miw.monitora.agent.model.Cliente;

public class PersistenceTest {

	private EntityManagerFactory factory;
	private Agente agente;
	private Cliente cliente;
	private List<Object> graph;

	@Before
	public void setUp() {
		factory = Persistence.createEntityManagerFactory("monitora_ag");
		graph = createGraph();
		persistGraph(graph);
	}

	@After
	public void tearDown() {
		removeGraph(graph);
		factory.close();
	}

	@Test
	public void testAgente() {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Agente ag = (Agente) mapper.merge(agente);

		//asserts
		assertNotNull(ag.getDestinos());

		trx.commit();
		mapper.close();
	}

	private void persistGraph(List<Object> graph) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : graph) {
			mapper.persist(o);
		}

		trx.commit();
		mapper.close();
	}

	private List<Object> createGraph() {
		List<Object> res = new LinkedList<Object>();
		agente = new Agente();
		cliente = new Cliente();
		
		agente.setCliente(cliente);
		cliente.addAgente(agente);		

		res.add(agente);
		res.add(cliente);

		return res;
	}

	private void removeGraph(List<Object> graph) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : graph) {
			mapper.remove(o);
		}

		trx.commit();
		mapper.close();
	}

}
