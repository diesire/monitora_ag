package es.uniovi.miw.monitora.agent.core;

import es.uniovi.miw.monitora.agent.snapshot.SnapshotManager;
import es.uniovi.miw.monitora.agent.task.quartz.QuartzTaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.SnapshotJobListener;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class AgentServiceFactory {

	public static SchedulerService getSchedulerService()
			throws BusinessException {
		QuartzTaskManager instance = QuartzTaskManager.getInstance();
		// FIXME multiple listeners!!!
		instance.setJobListener(new SnapshotJobListener("snapshotManager",
				getSnapshotManager()));
		return instance;
	}

	public static SnapshotManager getSnapshotManager() throws BusinessException {
		SnapshotManager instance = SnapshotManager.getInstance();
		return instance;
	}
}
