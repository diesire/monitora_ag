package es.uniovi.miw.monitora.agent;

import es.uniovi.miw.monitora.agent.core.MonitoraAgent;

public class Main {
	public static void main(String[] args) {
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
