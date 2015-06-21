package es.uniovi.miw.monitora.agent.task.quartz;

public class ConsultaDBJob extends ConsultaJob {
	private String sql_select;

	public String getSql_select() {
		return sql_select;
	}

	public void setSql_select(String sql_select) {
		this.sql_select = sql_select;
	}

	@Override
	protected Object run() {
		// TODO Auto-generated method stub
		return null;
	}
}
