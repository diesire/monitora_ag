package es.uniovi.miw.monitora.agent.snapshot;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.core.api.Task;
import es.uniovi.miw.monitora.core.snapshot.Snapshot;
import es.uniovi.miw.monitora.core.snapshot.StdoutResult;
import es.uniovi.miw.monitora.core.snapshot.TaskResult;

public class SnapshotManager {
	static Logger logger = LoggerFactory.getLogger(SnapshotManager.class);
	
	private Snapshot snapshot;
	
	public SnapshotManager(List<Task> tasks) {
		snapshot = new Snapshot();
		snapshot.creationDate = Calendar.getInstance();
		snapshot.tasks = tasks;
	}
	
	public synchronized void addResult(TaskResult result) {
		//TODO use lock mechanism instead?
		snapshot.results.add(result);
		logger.debug("add result {} to snapshot {}", result, snapshot);
	}

	public Snapshot getSnapshot() {
		//TODO use lock mechanism instead?
		return snapshot;
	}

	public List<Task> getTasks() {
		return snapshot.tasks;
	}

	public TaskResult createResult(String resultType, Object resultValue) {
		switch (resultType) {
		case TaskResult.STDOUT:
			StdoutResult stdoutResult = new StdoutResult();
			stdoutResult.output = (String) resultValue;
			return stdoutResult;
		default:
			logger.error("Unknow TaskResult type {}", resultType);
			return null;
		}
	}
}