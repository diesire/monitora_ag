package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.uniovi.miw.monitora.agent.model.keys.InformeTipoDestinoPK;


/**
 * The persistent class for the INFORME_TIPO_DESTINO database table.
 * 
 */
@Entity
@Table(name="INFORME_TIPO_DESTINO")
public class InformeTipoDestino implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InformeTipoDestinoPK id;

	@Column(name="POR_DEFECTO")
	private String porDefecto;

	//bi-directional many-to-one association to Informe
	@ManyToOne
	@JoinColumn(name="ID_INFORME", insertable=false, updatable=false)
	private Informe informe;

	//bi-directional many-to-one association to TipoDestino
	@ManyToOne
	@JoinColumn(name="ID_TIPO_DESTINO", insertable=false, updatable=false)
	private TipoDestino tipoDestino;

	public InformeTipoDestino() {
	}

	public InformeTipoDestinoPK getId() {
		return this.id;
	}

	public void setId(InformeTipoDestinoPK id) {
		this.id = id;
	}

	public String getPorDefecto() {
		return this.porDefecto;
	}

	public void setPorDefecto(String porDefecto) {
		this.porDefecto = porDefecto;
	}

	public Informe getInforme() {
		return this.informe;
	}

	public void setInforme(Informe informe) {
		this.informe = informe;
	}

	public TipoDestino getTipoDestino() {
		return this.tipoDestino;
	}

	public void setTipoDestino(TipoDestino tipoDestino) {
		this.tipoDestino = tipoDestino;
	}

}