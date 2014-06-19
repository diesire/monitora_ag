package es.uniovi.miw.monitora.agent.model.keys;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the INF_PLAN_DEST_BORRADO database table.
 * 
 */
@Embeddable
public class InfPlanDestBorradoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_INFORME")
	private int idInforme;

	@Column(name="ID_CLIENTE")
	private int idCliente;

	@Column(name="ID_DESTINO")
	private int idDestino;

	@Column(name="ID_PLAN")
	private int idPlan;

	public InfPlanDestBorradoPK() {
	}
	public int getIdInforme() {
		return this.idInforme;
	}
	public void setIdInforme(int idInforme) {
		this.idInforme = idInforme;
	}
	public int getIdCliente() {
		return this.idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public int getIdDestino() {
		return this.idDestino;
	}
	public void setIdDestino(int idDestino) {
		this.idDestino = idDestino;
	}
	public int getIdPlan() {
		return this.idPlan;
	}
	public void setIdPlan(int idPlan) {
		this.idPlan = idPlan;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InfPlanDestBorradoPK)) {
			return false;
		}
		InfPlanDestBorradoPK castOther = (InfPlanDestBorradoPK)other;
		return 
			(this.idInforme == castOther.idInforme)
			&& (this.idCliente == castOther.idCliente)
			&& (this.idDestino == castOther.idDestino)
			&& (this.idPlan == castOther.idPlan);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idInforme;
		hash = hash * prime + this.idCliente;
		hash = hash * prime + this.idDestino;
		hash = hash * prime + this.idPlan;
		
		return hash;
	}
}