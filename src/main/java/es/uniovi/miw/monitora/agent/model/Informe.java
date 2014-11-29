package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


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
	private Set<InformeConsulta> informeConsultas = new HashSet<InformeConsulta>();

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
	private Set<Informe> contenidos = new HashSet<Informe>();

	//bi-directional many-to-many association to Informe
	@ManyToMany(mappedBy="contenidos")
	private Set<Informe> contenedores = new HashSet<Informe>();

	//bi-directional many-to-one association to InformeTipoDestino
	@OneToMany(mappedBy="informe")
	private Set<InformeTipoDestino> informeTipoDestinos = new HashSet<InformeTipoDestino>();

	//bi-directional many-to-one association to InfPlanDest
	@OneToMany(mappedBy="informe")
	private Set<InfPlanDest> infPlanDests = new HashSet<InfPlanDest>();

	//bi-directional many-to-one association to Snapshot
	@OneToMany(mappedBy="informe", orphanRemoval = true)
	private Set<Snapshot> snapshots = new HashSet<Snapshot>();

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

	public Set<Informe> getContenidos() {
		return this.contenidos;
	}

	public void setContenidos(Set<Informe> contenidos) {
		this.contenidos = contenidos;
	}

	public Set<Informe> getContenedores() {
		return this.contenedores;
	}

	public void setContenedores(Set<Informe> contenedores) {
		this.contenedores = contenedores;
	}
	
	public Informe addContenido(Informe informe) {
		getContenidos().add(informe);
		informe.getContenedores().add(this);
		
		return informe;
	}
	
	public Informe removeContenido(Informe informe) {
		informe.getContenedores().remove(this);
		getContenidos().remove(informe);
		
		return informe;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + infoId;
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
		Informe other = (Informe) obj;
		if (infoId != other.infoId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Informe [infoId=").append(infoId)
				.append(", descLarga=").append(descLarga)
				.append(", fUltimaModificacion=").append(fUltimaModificacion)
				.append(", idPlan=").append(idPlan).append(", nombre=")
				.append(nombre).append("]");
		return builder.toString();
	}
	
	
}