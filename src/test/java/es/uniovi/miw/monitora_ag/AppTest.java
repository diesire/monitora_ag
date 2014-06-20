package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.agent.task.TaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.QuartzTaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.SnapshotJobListener;
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
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		String clientId = "AppTestClient";
		WebTarget resourceTarget = target.path("c2/ping/" + clientId);

		Ack ack = (Ack) resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.get(Ack.class);

		assertNotNull(ack.getUpdate().getTime());
	}

	@Test
	public void testRawTasks() {
		List<Task> tasks = getTasks();
		assertEquals(3, tasks.size());
	}

	@Test
	public void testQuartz() {
		TaskManager taskManager = new QuartzTaskManager();
		taskManager.start();
		taskManager.add(getTasks());
		try {
			Thread.sleep(2000);
			assertEquals(3, taskManager.size());
			taskManager.stop();
		} catch (InterruptedException e) {
			fail();
		}
	}
	
	@Test
	public void testSnapshot() {
		SnapshotManager snapshotManager = new SnapshotManager(getTasks());
		QuartzTaskManager taskManager = new QuartzTaskManager();
		SnapshotJobListener snapshotJobListener = new SnapshotJobListener("snapshot", snapshotManager);
		taskManager.start();
		taskManager.add(snapshotManager.getTasks());
		taskManager.setJobListener(snapshotJobListener);
		try {
			Thread.sleep(10000);
			assertEquals(3, taskManager.size());
			taskManager.stop();
		} catch (InterruptedException e) {
			fail();
		}
		assertEquals(3, snapshotManager.getSnapshot().tasks.size());
		assertNotNull(snapshotManager.getSnapshot().results.size());
	}

	private List<Task> getTasks() {
		Client restClient = ClientBuilder.newClient();
		WebTarget target = restClient
				.target("http://localhost:8080/monitora_sv/rest/");
		String clientId = "AppTestClient";
		WebTarget resourceTarget = target.path("c2/tasks/" + clientId);

		List<Task> tasks = resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Task>>() {
				});
		return tasks;
	}
}
