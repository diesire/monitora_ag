package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uniovi.miw.monitora.agent.model.keys.InformeConsultaPK;


/**
 * The persistent class for the INFORME_CONSULTA database table.
 * 
 */
@Entity
@Table(name="INFORME_CONSULTA")
public class InformeConsulta implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InformeConsultaPK id;

	@Temporal(TemporalType.DATE)
	@Column(name="F_ULTIMA_MODIFICACION")
	private Date fUltimaModificacion;

	//bi-directional many-to-one association to Consulta
	@ManyToOne
	@JoinColumn(name="ID_CONSULTA", insertable=false, updatable=false)
	private Consulta consulta;

	//bi-directional many-to-one association to Informe
	@ManyToOne
	@JoinColumn(name="ID_INFORME", insertable=false, updatable=false)
	private Informe informe;

	public InformeConsulta() {
	}

	public InformeConsultaPK getId() {
		return this.id;
	}

	public void setId(InformeConsultaPK id) {
		this.id = id;
	}

	public Date getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(Date fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public Consulta getConsulta() {
		return this.consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Informe getInforme() {
		return this.informe;
	}

	public void setInforme(Informe informe) {
		this.informe = informe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		InformeConsulta other = (InformeConsulta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InformeConsulta [id=").append(id)
				.append(", fUltimaModificacion=").append(fUltimaModificacion)
				.append("]");
		return builder.toString();
	}
	
	
}