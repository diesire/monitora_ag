package es.uniovi.miw.monitora.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.client.MonitoraClient;
import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.agent.task.TaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.QuartzTaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.SnapshotJobListener;
import es.uniovi.miw.monitora.core.api.Ack;
import es.uniovi.miw.monitora.core.api.Task;
import es.uniovi.miw.monitora.core.snapshot.TaskResult;
import es.uniovi.miw.monitora.core.task.Command;
import es.uniovi.miw.monitora.core.task.CommandType;
import es.uniovi.miw.monitora.core.task.SchedulerType;

/**
 * Unit test for simple App.
 */
public class OperationsTest {
	static Logger logger = LoggerFactory.getLogger(App.class);
	ArrayList<Task> serverTasks;
	private String clientId;

	@Before
	public void setUp() throws Exception {
		clientId = "AppTestClient";
		serverTasks = new ArrayList<Task>();
		serverTasks.add(createTask(clientId, new Command(CommandType.SHELL,
				"ls", "-la"), TaskResult.STDOUT, SchedulerType.CRON,
				"1 * * * * ?"));
		serverTasks.add(createTask(clientId, new Command(CommandType.SHELL,
				"touch", "deleteme"), TaskResult.STDOUT, SchedulerType.CRON,
				"1 * * * * ?"));
		serverTasks.add(createTask(clientId, new Command(CommandType.QUERY,
				"select * from system"), TaskResult.STDOUT, SchedulerType.CRON,
				"1 * * * * ?"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPing() {
		Ack expectedAck = new Ack();
		expectedAck.setUpdate(Calendar.getInstance());

		MonitoraClient mockedClient = mock(new MonitoraClient(clientId)
				.getClass());
		when(mockedClient.ping()).thenReturn(expectedAck);

		Ack ack = mockedClient.ping();

		assertNotNull(ack.getUpdate().getTime());
		assertEquals(expectedAck, ack);
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
		SnapshotJobListener snapshotJobListener = new SnapshotJobListener(
				"snapshot", snapshotManager);
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

		Builder mockedBuilder = mock(resourceTarget.request()
				.header("Content-Type", MediaType.APPLICATION_JSON).getClass());

		when(mockedBuilder.get(new GenericType<List<Task>>() {
		})).thenReturn(serverTasks);

		return mockedBuilder.get(new GenericType<List<Task>>() {
		});
	}

	private Task createTask(String clientId, Command command,
			String resultType, String scheduler, String schedulerArgs) {
		Task task = new Task();
		task.setCommand(command);
		task.setScheduler(scheduler);
		task.setSchedulerArgs(schedulerArgs);
		task.setId(clientId + System.nanoTime());
		task.setCreationDate(Calendar.getInstance());
		task.setResultType(resultType);
		logger.debug("created new {}", task);
		return task;
	}
}
