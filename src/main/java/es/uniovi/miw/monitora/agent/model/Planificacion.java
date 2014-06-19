package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the PLANIFICACION database table.
 * 
 */
@Entity
public class Planificacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PLAN")
	private int idPlan;

	private String descripcion;

	@Temporal(TemporalType.DATE)
	@Column(name="F_ULTIMA_MODIFICACION")
	private Date fUltimaModificacion;

	//bi-directional many-to-one association to InfPlanDest
	@OneToMany(mappedBy="planificacion")
	private Set<InfPlanDest> infPlanDests;

	//bi-directional many-to-one association to LineaCron
	@OneToMany(mappedBy="planificacion")
	private Set<LineaCron> lineaCrons;

	public Planificacion() {
	}

	public int getIdPlan() {
		return this.idPlan;
	}

	public void setIdPlan(int idPlan) {
		this.idPlan = idPlan;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(Date fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public Set<InfPlanDest> getInfPlanDests() {
		return this.infPlanDests;
	}

	public void setInfPlanDests(Set<InfPlanDest> infPlanDests) {
		this.infPlanDests = infPlanDests;
	}

	public InfPlanDest addInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().add(infPlanDest);
		infPlanDest.setPlanificacion(this);

		return infPlanDest;
	}

	public InfPlanDest removeInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().remove(infPlanDest);
		infPlanDest.setPlanificacion(null);

		return infPlanDest;
	}

	public Set<LineaCron> getLineaCrons() {
		return this.lineaCrons;
	}

	public void setLineaCrons(Set<LineaCron> lineaCrons) {
		this.lineaCrons = lineaCrons;
	}

	public LineaCron addLineaCron(LineaCron lineaCron) {
		getLineaCrons().add(lineaCron);
		lineaCron.setPlanificacion(this);

		return lineaCron;
	}

	public LineaCron removeLineaCron(LineaCron lineaCron) {
		getLineaCrons().remove(lineaCron);
		lineaCron.setPlanificacion(null);

		return lineaCron;
	}

}