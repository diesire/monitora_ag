package es.uniovi.miw.monitora.agent;

import java.util.concurrent.TimeUnit;

import es.uniovi.miw.monitora.agent.core.MonitoraAgent;
import es.uniovi.miw.monitora.agent.core.Status;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class Main {
	public static void main(String[] args) throws BusinessException {
		MonitoraAgent ag = new MonitoraAgent();
		try {
			ag.start();
			ag.pingServer();
			ag.updateTasks();
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				throw new BusinessException(e);
			}
			ag.sendResults();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ag.stop();
			ag.exit();
		}
		System.out.println("monitora_ag exit");
	}
}
