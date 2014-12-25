package es.uniovi.miw.monitora.agent.model.keys;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The primary key class for the DESTINO database table.
 * 
 */
@Embeddable
public class DestinoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ID_CLIENTE", insertable=false, updatable=false)
	private int idCliente;

	@Column(name="ID_DESTINO")
	private int idDestino;

	public DestinoPK() {
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

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DestinoPK)) {
			return false;
		}
		DestinoPK castOther = (DestinoPK)other;
		return 
			(this.idCliente == castOther.idCliente)
			&& (this.idDestino == castOther.idDestino);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idCliente;
		hash = hash * prime + this.idDestino;
		
		return hash;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DestinoPK [idCliente=").append(idCliente)
				.append(", idDestino=").append(idDestino).append("]");
		return builder.toString();
	}
	
	
}