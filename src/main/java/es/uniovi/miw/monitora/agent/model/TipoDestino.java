package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import javax.persistence.*;
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
	private Set<Consulta> consultas;

	//bi-directional many-to-one association to InformeTipoDestino
	@OneToMany(mappedBy="tipoDestino")
	private Set<InformeTipoDestino> informeTipoDestinos;

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

}