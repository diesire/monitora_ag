package es.uniovi.miw.monitora.agent;

import es.uniovi.miw.monitora.agent.core.MonitoraAgent;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class Main {
	public static void main(String[] args) throws BusinessException {
		MonitoraAgent ag = new MonitoraAgent();
		try {
			ag.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ag.stop();
			ag.exit();
		}
	}
}
