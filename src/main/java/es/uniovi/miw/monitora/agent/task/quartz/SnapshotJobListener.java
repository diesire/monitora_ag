package es.uniovi.miw.monitora.agent.task.quartz;

import java.util.GregorianCalendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.core.snapshot.TaskResult;

public class SnapshotJobListener extends JobListenerSupport {

	static private Logger logger = LoggerFactory
			.getLogger(SnapshotJobListener.class);

	private String name;
	private SnapshotManager snapshotManager;

	public SnapshotJobListener(String name, SnapshotManager snapshotManager) {
		this.name = name;
		this.snapshotManager = snapshotManager;
	}

	public String getName() {
		return name;
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		TaskResult result = snapshotManager.createResult((String) context
				.getJobDetail().getJobDataMap().getString("resultType"), context.getResult());
		result.startDate = new GregorianCalendar();
		result.startDate.setTime(context.getFireTime());
		result.taskId = (String) context.getJobDetail().getJobDataMap()
				.getString("taskId");
		logger.debug("created {}", result);

		snapshotManager.addResult(result);
	}
}
