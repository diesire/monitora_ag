package es.uniovi.miw.monitora.agent.task.quartz;

import java.net.URI;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.server.model.Consulta;
import es.uniovi.miw.monitora.server.model.Snapshot;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;

public class SnapshotJobListener extends JobListenerSupport {

	static private Logger logger = LoggerFactory
			.getLogger(SnapshotJobListener.class);

	private String name;
	private SnapshotManager snapshotManager;
	private PersistenceService db;

	public SnapshotJobListener(String name, SnapshotManager snapshotManager) {
		this.name = name;
		this.snapshotManager = snapshotManager;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		ConsultaJob jobResult = (ConsultaJob) context.getResult();
		if (jobResult == null) {
			logger.error("Job failed", context);
		}
		logger.info("Job done");
		try {
			snapshotManager.add(jobResult);
			logger.info("jobResult added");
		} catch (BusinessException e) {
			logger.error("jobResult failed" + e.getLocalizedMessage());
		}
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		super.jobToBeExecuted(context);
		logger.info("TOBE" + context);
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.warn("Vetoed" + context);
	}

}
