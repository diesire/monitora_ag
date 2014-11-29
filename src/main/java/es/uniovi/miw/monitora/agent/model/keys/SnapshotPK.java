package es.uniovi.miw.monitora.agent.model.keys;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the SNAPSHOT database table.
 * 
 */
@Embeddable
public class SnapshotPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_SNAPSHOT")
	private int idSnapshot;

	@Column(name="ID_CLIENTE", insertable=false, updatable=false)
	private int idCliente;

	@Column(name="ID_DESTINO", insertable=false, updatable=false)
	private int idDestino;

	@Column(name="ID_INFORME", insertable=false, updatable=false)
	private int idInforme;

	public SnapshotPK() {
	}
	public int getIdSnapshot() {
		return this.idSnapshot;
	}
	public void setIdSnapshot(int idSnapshot) {
		this.idSnapshot = idSnapshot;
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
	public int getIdInforme() {
		return this.idInforme;
	}
	public void setIdInforme(int idInforme) {
		this.idInforme = idInforme;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SnapshotPK)) {
			return false;
		}
		SnapshotPK castOther = (SnapshotPK)other;
		return 
			(this.idSnapshot == castOther.idSnapshot)
			&& (this.idCliente == castOther.idCliente)
			&& (this.idDestino == castOther.idDestino)
			&& (this.idInforme == castOther.idInforme);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idSnapshot;
		hash = hash * prime + this.idCliente;
		hash = hash * prime + this.idDestino;
		hash = hash * prime + this.idInforme;
		
		return hash;
	}
}