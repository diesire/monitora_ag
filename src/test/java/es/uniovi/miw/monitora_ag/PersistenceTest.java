package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hsqldb.lib.InOutUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.model.Agente;
import es.uniovi.miw.monitora.agent.model.Cliente;
import es.uniovi.miw.monitora.agent.model.Consulta;
import es.uniovi.miw.monitora.agent.model.Informe;
import es.uniovi.miw.monitora.agent.model.InformeConsulta;
import es.uniovi.miw.monitora.agent.model.InformeTipoDestino;
import es.uniovi.miw.monitora.agent.model.TipoDestino;

public class PersistenceTest {

	static Logger logger = LoggerFactory.getLogger(PersistenceTest.class);
	private EntityManagerFactory factory;
	private Agente agente;
	private Cliente cliente;
	private Informe informe;
	private List<Object> graph;
	private Consulta consulta;
	private InformeConsulta informeConsulta;
	private TipoDestino tipoDestino;
	private InformeTipoDestino informeTipoDestino;

	@Before
	public void setUp() {
//		logger.debug("setUp");
		factory = Persistence.createEntityManagerFactory("monitora_ag");
		graph = createGraph();
		persistGraph(graph);
	}

	@After
	public void tearDown() {
//		logger.debug("tearDown");
		removeGraph();
		factory.close();
	}

	
	public void testAgente() {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Agente ag = (Agente) mapper.merge(agente);

		assertNotNull(ag.getCliente());

		trx.commit();
		mapper.close();
	}

	
	public void testCliente() {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		Cliente cl = (Cliente) mapper.merge(cliente);

		assertEquals("Cliente", cl.getNombre());

		trx.commit();
		mapper.close();
	}
	
	@Test
	public void testInforme() {
		EntityManager mapper= factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();
		
		Informe in = (Informe) mapper.merge(informe);
		assertEquals("Nombre", in.getNombre());

		trx.commit();
		mapper.close();
	}
	
	@Test
	public void testConsulta() {
		EntityManager mapper= factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();
		
		Consulta co = (Consulta) mapper.merge(consulta);
		assertEquals("S", co.getTipo());

		trx.commit();
		mapper.close();
	}
	
	@Test
	public void testInformeConsulta() {
		EntityManager mapper= factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();
		
		Informe in = (Informe) mapper.merge(informe);
		Consulta co = (Consulta) mapper.merge(consulta);
		assertTrue(in.getInformeConsultas().containsAll(co.getInformeConsultas()));

		trx.commit();
		mapper.close();
	}
	
	@Test
	public void testTipoDestino() {
		EntityManager mapper= factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();
		
		TipoDestino tDes = (TipoDestino) mapper.merge(tipoDestino);
		Consulta co = (Consulta) mapper.merge(consulta);
		
		assertTrue(tDes.getConsultas().contains(co));
		assertTrue(co.getTipoDestinos().contains(tDes));

		trx.commit();
		mapper.close();
	}
	
	@Test
	public void testInformeTipoDestino() {
		EntityManager mapper= factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();
		
		TipoDestino tDes = (TipoDestino) mapper.merge(tipoDestino);
		Informe co = (Informe) mapper.merge(informe);
		
		assertTrue(tDes.getInformeTipoDestinos().containsAll(co.getInformeTipoDestinos()));

		trx.commit();
		mapper.close();
	}

	private void persistGraph(List<Object> graph) {
//		logger.debug("persistGraph {}", graph);
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : graph) {
			mapper.persist(o);
//			logger.debug("\t + {}", o);
		}

		trx.commit();
		mapper.close();
	}

	private List<Object> createGraph() {
//		logger.debug("createGraph");
		List<Object> res = new LinkedList<Object>();
		
		informe = new Informe();
		informe.setNombre("Nombre");
		informe.setDescLarga("DescLarga");
		informe.setFUltimaModificacion(new Date(System.currentTimeMillis()));
		informe.setIdPlan(0);
		
		consulta = new Consulta();
		consulta.setTipo("S");
		consulta.setDescCorta("DescCorta");
		consulta.setDescLarga("DescLarga");
		consulta.setFUltimaModificacion(new Date(System.currentTimeMillis()));
		
		informeConsulta = new InformeConsulta();
		informeConsulta.setFUltimaModificacion(new Date(System.currentTimeMillis()));
		
		informeTipoDestino = new InformeTipoDestino();
		informeTipoDestino.setPorDefecto("PorDefecto");
		
		tipoDestino = new TipoDestino();
		tipoDestino.setDescripcion("Descripción");
		tipoDestino.setNombreCorto("NombreCorto");
		
		
		
		
//		cliente = new Cliente();
//		cliente.setNombre("Cliente");
//		agente = new Agente();
		
		
		
		//link
//		agente.setCliente(cliente);
//		cliente.addAgente(agente);
		informe.addInformeConsulta(informeConsulta);
		consulta.addInformeConsulta(informeConsulta);
		
		consulta.addTipoDestino(tipoDestino);
		tipoDestino.addConsulta(consulta);
		
		informe.addInformeTipoDestino(informeTipoDestino);
		tipoDestino.addInformeTipoDestino(informeTipoDestino);

//		res.add(cliente);
//		res.add(agente);
		res.add(informe);
		res.add(consulta);
		res.add(tipoDestino);

//		logger.debug("\t -> {}", res);
		return res;
	}

	private List<Object> mergeGraph(EntityManager mapper) {
//		logger.debug("mergeGraph (from mapper {})", mapper);
		List<Object> res = new LinkedList<Object>();
//		Cliente cl = mapper.merge(cliente);
//		Agente ag = mapper.merge(agente);
		Informe in = mapper.merge(informe);
		Consulta cons = mapper.merge(consulta);
		TipoDestino tDes = mapper.merge(tipoDestino);

//		res.add(cl);
//		res.add(ag);
		res.add(in);
		res.add(cons);
		res.add(tDes);

//		logger.debug("\t -> {}", res);
		return res;
	}

	private void removeGraph() {
//		logger.debug("removeGraph");
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for (Object o : mergeGraph(mapper)) {
			mapper.remove(o);
//			logger.debug("\t - {}", o);
		}

		trx.commit();
		mapper.close();
	}

}
