package es.uniovi.miw.monitora.agent.model.keys;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the INF_PLAN_DEST database table.
 * 
 */
@Embeddable
public class InfPlanDestPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_INFORME", insertable=false, updatable=false)
	private int idInforme;

	@Column(name="ID_CLIENTE", insertable=false, updatable=false)
	private int idCliente;

	@Column(name="ID_DESTINO", insertable=false, updatable=false)
	private int idDestino;

	public InfPlanDestPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InfPlanDestPK)) {
			return false;
		}
		InfPlanDestPK castOther = (InfPlanDestPK)other;
		return 
			(this.idInforme == castOther.idInforme)
			&& (this.idCliente == castOther.idCliente)
			&& (this.idDestino == castOther.idDestino);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idInforme;
		hash = hash * prime + this.idCliente;
		hash = hash * prime + this.idDestino;
		
		return hash;
	}
}