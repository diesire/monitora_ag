package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uniovi.miw.monitora.agent.model.keys.InfPlanDestPK;

/**
 * The persistent class for the INF_PLAN_DEST database table.
 * 
 */
@Entity
@Table(name = "INF_PLAN_DEST")
public class InfPlanDest implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InfPlanDestPK id = new InfPlanDestPK();

	@Temporal(TemporalType.DATE)
	@Column(name = "F_ULTIMA_APLICACION")
	private Date fUltimaAplicacion;

	@Temporal(TemporalType.DATE)
	@Column(name = "F_ULTIMA_MODIFICACION")
	private Date fUltimaModificacion;

	// bi-directional many-to-one association to Destino
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID_CLIENTE", insertable = false, updatable = false),
			@JoinColumn(name = "ID_DESTINO", referencedColumnName = "ID_DESTINO", insertable = false, updatable = false) })
	private Destino destino;

	// bi-directional many-to-one association to Informe
	@ManyToOne
	@JoinColumn(name = "ID_INFORME", insertable = false, updatable = false)
	private Informe informe;

	// bi-directional many-to-one association to Planificacion
	@ManyToOne
	@JoinColumn(name = "ID_PLAN")
	private Planificacion planificacion;

	public InfPlanDest() {
	}

	public InfPlanDestPK getId() {
		return this.id;
	}

	public void setId(InfPlanDestPK id) {
		this.id = id;
	}

	public Date getFUltimaAplicacion() {
		return this.fUltimaAplicacion;
	}

	public void setFUltimaAplicacion(Date fUltimaAplicacion) {
		this.fUltimaAplicacion = fUltimaAplicacion;
	}

	public Date getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(Date fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public Destino getDestino() {
		return this.destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
		getId().setIdDestino(destino.getId().getIdDestino());
		getId().setIdCliente(destino.getId().getIdCliente());
	}

	public Informe getInforme() {
		return this.informe;
	}

	public void setInforme(Informe informe) {
		this.informe = informe;
		getId().setIdInforme(informe.getInfoId());
	}

	public Planificacion getPlanificacion() {
		return this.planificacion;
	}

	public void setPlanificacion(Planificacion planificacion) {
		this.planificacion = planificacion;
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
		InfPlanDest other = (InfPlanDest) obj;
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
		builder.append("InfPlanDest [id=").append(id)
				.append(", fUltimaAplicacion=").append(fUltimaAplicacion)
				.append(", fUltimaModificacion=").append(fUltimaModificacion)
				.append("]");
		return builder.toString();
	}

}