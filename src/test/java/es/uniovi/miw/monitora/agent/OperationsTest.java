package es.uniovi.miw.monitora.agent;


/**
 * Unit test for simple App.
 */
public class OperationsTest {
//	private static final Calendar NOW = Calendar.getInstance();
//	private static final String TEST_CLIENT = "AppTestClient";
//
//	static Logger logger = LoggerFactory.getLogger(App.class);
//	private MonitoraClient mockedClient;
//
//	@Before
//	public void setUp() throws Exception {
//		ArrayList<Task> expectedTasks = new ArrayList<Task>();
//		Ack expectedAck = new Ack();
//
//		expectedTasks.add(createTask(TEST_CLIENT, new Command(
//				CommandType.SHELL, "ls", "-la"), TaskResult.STDOUT,
//				SchedulerType.CRON, "1 * * * * ?"));
//		expectedTasks.add(createTask(TEST_CLIENT, new Command(
//				CommandType.SHELL, "touch", "deleteme"), TaskResult.STDOUT,
//				SchedulerType.CRON, "1 * * * * ?"));
//		expectedTasks.add(createTask(TEST_CLIENT, new Command(
//				CommandType.QUERY, "select * from system"), TaskResult.STDOUT,
//				SchedulerType.CRON, "1 * * * * ?"));
//
//		expectedAck.setUpdate(NOW);
//
//		mockedClient = mock(new MonitoraClient(TEST_CLIENT).getClass());
//		when(mockedClient.getTasks()).thenReturn(expectedTasks);
//		when(mockedClient.ping()).thenReturn(expectedAck);
//	}
//
//	@Test
//	public void testRawTasks() {
//		List<Task> tasks = mockedClient.getTasks();
//		
//		assertEquals(3, tasks.size());
//	}
//
//	@Test
//	public void testQuartz() {
//
//		TaskManager taskManager = new QuartzTaskManager();
//
//		taskManager.start();
//		taskManager.add(mockedClient.getTasks());
//		try {
//
//			Thread.sleep(2000);
//			assertEquals(3, taskManager.size());
//			taskManager.stop();
//		} catch (InterruptedException e) {
//
//			fail();
//		}
//	}
//
//	@Test
//	public void testSnapshot() {
//
//		SnapshotManager snapshotManager = new SnapshotManager(
//				mockedClient.getTasks());
//		QuartzTaskManager taskManager = new QuartzTaskManager();
//		SnapshotJobListener snapshotJobListener = new SnapshotJobListener(
//				"snapshot", snapshotManager);
//
//		taskManager.start();
//		taskManager.add(snapshotManager.getTasks());
//		taskManager.setJobListener(snapshotJobListener);
//		try {
//
//			Thread.sleep(10000);
//			assertEquals(3, taskManager.size());
//			taskManager.stop();
//		} catch (InterruptedException e) {
//
//			fail();
//		}
//
//		assertEquals(3, snapshotManager.getSnapshot().tasks.size());
//		assertNotNull(snapshotManager.getSnapshot().results.size());
//	}
//
//	private Task createTask(String clientId, Command command,
//			String resultType, String scheduler, String schedulerArgs) {
//		
//		Task task = new Task();
//		task.setCommand(command);
//		task.setScheduler(scheduler);
//		task.setSchedulerArgs(schedulerArgs);
//		task.setId(clientId + System.nanoTime());
//		task.setCreationDate(NOW);
//		task.setResultType(resultType);
//		logger.debug("created new {}", task);
//		return task;
//	}
}
