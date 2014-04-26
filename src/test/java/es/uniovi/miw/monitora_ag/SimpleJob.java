package es.uniovi.miw.monitora_ag;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {

	static public int counter;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		counter++;
		System.out.println(counter);
	}
}