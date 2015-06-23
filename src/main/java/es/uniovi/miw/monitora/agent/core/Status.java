package es.uniovi.miw.monitora.agent.core;

public enum Status {
	CREATED("Created"), RUNNING("Running"), STOPPED("Stopped"), TERMINATED(
			"Terminated");

	private String status;

	private Status(String status) {
		this.status = status;
	}
}
