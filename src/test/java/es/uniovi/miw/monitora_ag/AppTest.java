package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.App;
import es.uniovi.miw.monitora.agent.task.TaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.QuartzTaskManager;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.core.api.Task;

/**
 * Unit test for simple App.
 */
public class AppTest {
	static Logger logger = LoggerFactory.getLogger(App.class);
	
	@Test
	public void testPing() {
		Client restClient = ClientBuilder.newClient();
		logger.debug("Ping");
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		String clientId = "AppTestClient";
		WebTarget resourceTarget = target.path("c2/ping/" + clientId);

		Ack ack = (Ack) resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON).get(Ack.class);
		logger.debug("ack {}", ack.getUpdate().getTime());
		// assertEquals("OK", ack.getUpdate());
	}
	
	@Test
	public void testTasks() {
		List<Task> tasks = getTasks();
		logger.debug("tasks {}", tasks);
		// assertEquals("OK", ack.getUpdate());
	}

	private List<Task> getTasks() {
		Client restClient = ClientBuilder.newClient();
		logger.debug("Tasks");
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		String clientId = "AppTestClient";
		WebTarget resourceTarget = target.path("c2/tasks/" + clientId);

		List<Task> tasks = resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON).get(new GenericType<List<Task>>(){});
		return tasks;
	}
	
	@Test
	public void testSchedule() {
		List<Task> tasks = getTasks();
		TaskManager taskManager = new QuartzTaskManager();
		taskManager.start();
		taskManager.add(tasks);
		try {
			Thread.sleep(10000);
			assertEquals(2, taskManager.size());
			taskManager.stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
