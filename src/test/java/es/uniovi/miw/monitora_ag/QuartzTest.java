package es.uniovi.miw.monitora_ag;

import static org.junit.Assert.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {
	@Test
	public void test() {
		int times = 2;
		
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();
			doSomething(scheduler, times);
			Thread.sleep(10000);
			scheduler.shutdown();
			assertEquals(times, SimpleJob.counter);

		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void doSomething(Scheduler scheduler, int times)
			throws SchedulerException {
		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(SimpleJob.class).withIdentity("job1", "group1")
				.build();

		// Trigger the job to run now, and then repeat every 40 seconds
		Trigger trigger = newTrigger()
				.withIdentity("trigger1", "group1")
				.startNow()
				.withSchedule(
						simpleSchedule().withIntervalInSeconds(2)
								.repeatSecondlyForTotalCount(times)).build();

		// Tell quartz to schedule the job using our trigger
		scheduler.scheduleJob(job, trigger);
	}
}

