package es.uniovi.miw.monitora.agent.task.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.task.TaskManager;
import es.uniovi.miw.monitora.core.api.Task;
import es.uniovi.miw.monitora.core.task.SchedulerType;

public class QuartzTaskManager implements TaskManager {

	static Logger logger = LoggerFactory.getLogger(QuartzTaskManager.class);
	private Scheduler scheduler;

	public QuartzTaskManager() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// doSomething(scheduler, times);
		// Thread.sleep(10000);
	}

	public void add(List<Task> tasks) {
		for (Task task : tasks) {
			add(task);
		}
	}

	public void add(Task task) {
		scheduleJob(task);
	}

	private void scheduleJob(Task task) {
		try {
			String jobName = "job" + task.getId();
			String groupName = "group"
					+ task.getCreation().getTime().toString();
			String triggerName = "trigger" + task.getId();
			JobDetail job = newJob(CommandJob.class)
					.withIdentity(jobName, groupName)
					.usingJobData("args", task.getCommandArgs())
					.usingJobData("commandType", task.getCommandType()).build();

			Trigger trigger = newTrigger()
					.withIdentity(triggerName, groupName)
					.startNow()
					.withSchedule(
							createTriggerBuilder(task.getScheduler(),
									task.getSchedulerArgs())).build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ScheduleBuilder<? extends Trigger> createTriggerBuilder(
			String schedulerType, String schedulerArgs) {
		switch (schedulerType) {
		case SchedulerType.CRON:
			return CronScheduleBuilder.cronSchedule(schedulerArgs);
		case SchedulerType.MONTHLY:
			return CronScheduleBuilder.monthlyOnDayAndHourAndMinute(5, 15, 0); // TODO
																				// getParams
		default:
			logger.error("Unknow SchedulerType {}", schedulerType);
			return null;
		}
	}

	public void start() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int size() {
		int i = 0;
		try {
			for(String group: scheduler.getJobGroupNames()) {
			    // enumerate each job in group
				for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(group))) {
					i++;
			    }
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

}
