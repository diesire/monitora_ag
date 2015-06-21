package es.uniovi.miw.monitora.agent.task.quartz;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.uniovi.miw.monitora.agent.core.AgentServiceFactory;
import es.uniovi.miw.monitora.server.conf.PersistenceFactory;
import es.uniovi.miw.monitora.server.conf.ServicesFactory;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;
import es.uniovi.miw.monitora.server.persistence.util.PersistenceService;

public abstract class ConsultaJob implements Job {

	private String tabla;
	private String sql_create;
	private String sql_insert;
	private Integer idCliente;
	private Integer idDestino;
	private Integer idInforme;
	private Integer idConsulta;
	protected PersistenceService db;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		try {
			db = PersistenceFactory.getPersistenceService();
			context.setResult(run());
		} catch (BusinessException e) {
			context.setResult(null);
		}
	}

	protected abstract Object run() throws BusinessException;

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getSql_create() {
		return sql_create;
	}

	public void setSql_create(String sql_create) {
		this.sql_create = sql_create;
	}

	public String getSql_insert() {
		return sql_insert;
	}

	public void setSql_insert(String sql_insert) {
		this.sql_insert = sql_insert;
	}

	protected void createTable() throws BusinessException {
		Statement stat = null;
		try {
			stat = db.getConnection().createStatement();
			stat.execute(getSql_create());
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

	public void deleteData() throws BusinessException {
		PreparedStatement stat = null;
		try {
			stat = db.getConnection().prepareStatement(getSql_insert()); // FIXME
																			// Sql
																			// delete
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

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Integer idDestino) {
		this.idDestino = idDestino;
	}

	public Integer getIdInforme() {
		return idInforme;
	}

	public void setIdInforme(Integer idInforme) {
		this.idInforme = idInforme;
	}

	public Integer getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}
}
