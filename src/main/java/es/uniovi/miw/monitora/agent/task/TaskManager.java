package es.uniovi.miw.monitora.agent.task;

import java.util.List;

import es.uniovi.miw.monitora.core.api.Task;

public interface TaskManager {

	void add(List<Task> tasks);

	void start();

	void stop();

	void add(Task task);

	int size();
}
