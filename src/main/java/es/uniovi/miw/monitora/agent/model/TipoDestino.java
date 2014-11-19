package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;


/**
 * The persistent class for the TIPO_DESTINO database table.
 * 
 */
@Entity
@Table(name="TIPO_DESTINO")
public class TipoDestino implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TIPO_DESTINO")
	private int idTipoDestino;

	private String descripcion;

	@Column(name="NOMBRE_CORTO")
	private String nombreCorto;

	//bi-directional many-to-many association to Consulta
	@ManyToMany(mappedBy="tipoDestinos")
	private Set<Consulta> consultas = new HashSet<Consulta>();

	//bi-directional many-to-one association to InformeTipoDestino
	@OneToMany(mappedBy="tipoDestino")
	private Set<InformeTipoDestino> informeTipoDestinos = new HashSet<InformeTipoDestino>();

	public TipoDestino() {
	}

	public int getIdTipoDestino() {
		return this.idTipoDestino;
	}

	public void setIdTipoDestino(int idTipoDestino) {
		this.idTipoDestino = idTipoDestino;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombreCorto() {
		return this.nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public Set<Consulta> getConsultas() {
		return this.consultas;
	}

	public void setConsultas(Set<Consulta> consultas) {
		this.consultas = consultas;
	}
	
	public Consulta addConsulta(Consulta consulta) {
		getConsultas().add(consulta);
		
		return consulta;
	}
	
	public Consulta removeConsulta(Consulta consulta) {
		getConsultas().remove(consulta);
		
		return consulta;
	}

	public Set<InformeTipoDestino> getInformeTipoDestinos() {
		return this.informeTipoDestinos;
	}

	public void setInformeTipoDestinos(Set<InformeTipoDestino> informeTipoDestinos) {
		this.informeTipoDestinos = informeTipoDestinos;
	}

	public InformeTipoDestino addInformeTipoDestino(InformeTipoDestino informeTipoDestino) {
		getInformeTipoDestinos().add(informeTipoDestino);
		informeTipoDestino.setTipoDestino(this);

		return informeTipoDestino;
	}

	public InformeTipoDestino removeInformeTipoDestino(InformeTipoDestino informeTipoDestino) {
		getInformeTipoDestinos().remove(informeTipoDestino);
		informeTipoDestino.setTipoDestino(null);

		return informeTipoDestino;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTipoDestino;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoDestino other = (TipoDestino) obj;
		if (idTipoDestino != other.idTipoDestino)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TipoDestino [idTipoDestino=").append(idTipoDestino)
				.append(", descripcion=").append(descripcion)
				.append(", nombreCorto=").append(nombreCorto).append("]");
		return builder.toString();
	}
	
	

}