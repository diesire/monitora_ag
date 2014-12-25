package es.uniovi.miw.monitora.agent.model.keys;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The primary key class for the INFORME_TIPO_DESTINO database table.
 * 
 */
@Embeddable
public class InformeTipoDestinoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_INFORME", insertable = false, updatable = false)
	private int idInforme;

	@Column(name = "ID_TIPO_DESTINO", insertable = false, updatable = false)
	private int idTipoDestino;

	public InformeTipoDestinoPK() {
	}

	public int getIdInforme() {
		return this.idInforme;
	}

	public void setIdInforme(int idInforme) {
		this.idInforme = idInforme;
	}

	public int getIdTipoDestino() {
		return this.idTipoDestino;
	}

	public void setIdTipoDestino(int idTipoDestino) {
		this.idTipoDestino = idTipoDestino;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof InformeTipoDestinoPK)) {
			return false;
		}
		InformeTipoDestinoPK castOther = (InformeTipoDestinoPK) other;
		return (this.idInforme == castOther.idInforme)
				&& (this.idTipoDestino == castOther.idTipoDestino);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idInforme;
		hash = hash * prime + this.idTipoDestino;

		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InformeTipoDestinoPK [idInforme=").append(idInforme)
				.append(", idTipoDestino=").append(idTipoDestino).append("]");
		return builder.toString();
	}

}