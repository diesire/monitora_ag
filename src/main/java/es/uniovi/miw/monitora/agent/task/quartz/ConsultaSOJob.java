package es.uniovi.miw.monitora.agent.task.quartz;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import es.uniovi.miw.monitora.agent.shell.apache.ApacheCommomsExecManager;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class ConsultaSOJob extends ConsultaJob {
	private String comando_so;

	public String getComando_so() {
		return comando_so;
	}

	public void setComando_so(String comando_so) {
		this.comando_so = comando_so;
	}

	@Override
	protected Object run() throws BusinessException {

		try {
			createTable();
		} catch (Exception e) {
			// table created
		}

		insertData(new ApacheCommomsExecManager().run(comando_so));

		return this;
	}

	private void insertData(String result) throws BusinessException {
		// Statement stat = null;
		// try {
		// stat = db.getConnection().createStatement();
		// stat.execute(getSql_create());
		// } catch (SQLException e) {
		// throw new BusinessException(e);
		// } finally {
		// if (stat != null) {
		// try {
		// stat.close();
		// } catch (SQLException e) {
		// throw new BusinessException(e);
		// }
		// }
		// }
		PreparedStatement stat = null;
		try {
			stat = db.getConnection().prepareStatement(getSql_insert());
			stat.setString(1, result);
			stat.executeUpdate();
		} catch (SQLException e) {
			throw new BusinessException(e);
		} finally {
			if (stat != null) {
				try {
					stat.close();
				} catch (SQLException e) {
					throw new BusinessException(e);
				}
			}
		}
	}
}
