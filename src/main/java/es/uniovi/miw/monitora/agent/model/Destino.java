package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.uniovi.miw.monitora.agent.model.keys.DestinoPK;


/**
 * The persistent class for the DESTINO database table.
 * 
 */
@Entity
public class Destino implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DestinoPK id;

	@Column(name="ID_TIPO_DESTINO")
	private int idTipoDestino;

	//bi-directional many-to-many association to Agente
	@ManyToMany(mappedBy="destinos")
	private Set<Agente> agentes;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE", insertable=false, updatable=false)
	private Cliente cliente;

	//bi-directional many-to-one association to InfPlanDest
	@OneToMany(mappedBy="destino")
	private Set<InfPlanDest> infPlanDests;

	//bi-directional many-to-one association to Snapshot
	@OneToMany(mappedBy="destino")
	private Set<Snapshot> snapshots;

	public Destino() {
	}

	public DestinoPK getId() {
		return this.id;
	}

	public void setId(DestinoPK id) {
		this.id = id;
	}

	public int getIdTipoDestino() {
		return this.idTipoDestino;
	}

	public void setIdTipoDestino(int idTipoDestino) {
		this.idTipoDestino = idTipoDestino;
	}

	public Set<Agente> getAgentes() {
		return this.agentes;
	}

	public void setAgentes(Set<Agente> agentes) {
		this.agentes = agentes;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<InfPlanDest> getInfPlanDests() {
		return this.infPlanDests;
	}

	public void setInfPlanDests(Set<InfPlanDest> infPlanDests) {
		this.infPlanDests = infPlanDests;
	}

	public InfPlanDest addInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().add(infPlanDest);
		infPlanDest.setDestino(this);

		return infPlanDest;
	}

	public InfPlanDest removeInfPlanDest(InfPlanDest infPlanDest) {
		getInfPlanDests().remove(infPlanDest);
		infPlanDest.setDestino(null);

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
		snapshot.setDestino(this);

		return snapshot;
	}

	public Snapshot removeSnapshot(Snapshot snapshot) {
		getSnapshots().remove(snapshot);
		snapshot.setDestino(null);

		return snapshot;
	}

}