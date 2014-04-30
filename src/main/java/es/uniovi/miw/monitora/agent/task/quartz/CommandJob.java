package es.uniovi.miw.monitora.agent.task.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.uniovi.miw.monitora.core.task.Command;

public class CommandJob implements Job, Command {
	private String args;
	private String commandType;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		run();
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	@Override
	public void run() {
		System.out.println(args);
	}
}
