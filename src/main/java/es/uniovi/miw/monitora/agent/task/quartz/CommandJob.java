package es.uniovi.miw.monitora.agent.task.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import es.uniovi.miw.monitora.agent.shell.apache.ApacheCommomsExecManager;

public class CommandJob implements Job {
	private String taskId;
	private String commandArgs;
	private String commandType;
	private String resultType;

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		context.setResult(run());
	}

	public String getType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public Object run() {
		return new ApacheCommomsExecManager().run(commandArgs);
	}

	public String getCommandArgs() {
		return commandArgs;
	}

	public void setCommandArgs(String commandArgs) {
		this.commandArgs = commandArgs;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
