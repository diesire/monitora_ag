package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the INFORME database table.
 * 
 */
@Entity
public class Informe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="INFO_ID")
	private int infoId;

	@Column(name="DESC_LARGA")
	private String descLarga;

	@Temporal(TemporalType.DATE)
	@Column(name="F_ULTIMA_MODIFICACION")
	private Date fUltimaModificacion;

	@Column(name="ID_PLAN")
	private int idPlan;

	private String nombre;

	//bi-directional many-to-one association to InformeConsulta
	@OneToMany(mappedBy="informe")
	private Set<InformeConsulta> informeConsultas;

	//bi-directional many-to-many association to Informe
	@ManyToMany
	@JoinTable(
		name="INFORME_INFORME"
		, joinColumns={
			@JoinColumn(name="ID_INFORME_CONTENIDO")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_INFORME_CONTIENE")
			}
		)
	private Set<Informe> informes1;

	//bi-directional many-to-many association to Informe
	@ManyToMany(mappedBy="informes1")
	private Set<Informe> informes2;

	//bi-directional many-to-one association to InformeTipoDestino
	@OneToMany(mappedBy="informe")
	private Set<InformeTipoDestino> informeTipoDestinos;

	//bi-directional many-to-one association to InfPlanDest
	@OneToMany(mappedBy="informe")
	private Set<InfPlanDest> infPlanDests;

	//bi-directional many-to-one association to Snapshot
	@OneToMany(mappedBy="informe")
	private Set<Snapshot> snapshots;

	public Informe() {
	}

	public int getInfoId() {
		return this.infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public String getDescLarga() {
		return this.descLarga;
	}

	public void setDescLarga(String descLarga) {
		this.descLarga = descLarga;
	}

	public Date getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(Date fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public int getIdPlan() {
		return this.idPlan;
	}

	public void setIdPlan(int idPlan) {
		this.idPlan = idPlan;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<InformeConsulta> getInformeConsultas() {
		return this.informeConsultas;
	}

	public void setInformeConsultas(Set<InformeConsulta> informeConsultas) {
		this.informeConsultas = informeConsultas;
	}

	public InformeConsulta addInformeConsulta(InformeConsulta informeConsulta) {
		getInformeConsultas().add(informeConsulta);
		informeConsulta.setInforme(this);

		return informeConsulta;
	}

	public InformeConsulta removeInformeConsulta(InformeConsulta informeConsulta) {
		getInformeConsultas().remove(informeConsulta);
		informeConsulta.setInforme(null);

		return informeConsulta;
	}

	public Set<Informe> getInformes1() {
		return this.informes1;
	}

	public void setInformes1(Set<Informe> informes1) {
		this.informes1 = informes1;
	}

	public Set<Informe> getInformes2() {
		return this.informes2;
	}

	public void setInformes2(Set<Informe> informes2) {
		this.informes2 = informes2;
	}

	public Set<InformeTipoDestino> getInformeTipoDestinos() {
		return this.informeTipoDestinos;
	}

	public void setInformeTipoDestinos(Set<InformeTipoDestino> informeTipoDestinos) {
		this.informeTipoDestinos = informeTipoDestinos;
	}

	public InformeTipoDestino addInformeTipoDestino(InformeTipoDestino informeTipoDestino) {
		getInformeTipoDestinos().add(informeTipoDestino);
		informeTipoDestino.setInforme(this);

		return informeTipoDestino;
	}

	public InformeTipoDestino removeInformeTipoDestino(InformeTipoDestino informeTipoDestino) {
		getInformeTipoDestinos().remove(informeTipoDestino);
		informeTipoDestino.setInforme(null);

		return informeTipoDestino;
	}

	public Set<InfPlanDest> getInfPlanDests() {
		return this.infPlanDests;
	}

	public void setInfPlanDests(Set<InfPlanDest> infPlanDests) {
		this.infPlanDests = infPlanDests;
	}

	public InfPlanDest addInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().add(infPlanDest);
		infPlanDest.setInforme(this);

		return infPlanDest;
	}

	public InfPlanDest removeInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().remove(infPlanDest);
		infPlanDest.setInforme(null);

		return infPlanDest;
	}

	public Set<Snapshot> getSnapshots() {
		return this.snapshots;
	}

	public void setSnapshots(Set<Snapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public Snapshot addSnapshot(Snapshot snapshot) {
		getSnapshots().add(snapshot);
		snapshot.setInforme(this);

		return snapshot;
	}

	public Snapshot removeSnapshot(Snapshot snapshot) {
		getSnapshots().remove(snapshot);
		snapshot.setInforme(null);

		return snapshot;
	}

}