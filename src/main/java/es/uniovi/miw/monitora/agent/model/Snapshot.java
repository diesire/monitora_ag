package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uniovi.miw.monitora.agent.model.keys.SnapshotPK;


/**
 * The persistent class for the SNAPSHOT database table.
 * 
 */
@Entity
public class Snapshot implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SnapshotPK id;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	//bi-directional many-to-one association to Destino
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_CLIENTE", insertable=false, updatable=false),
		@JoinColumn(name="ID_DESTINO", referencedColumnName="ID_DESTINO", insertable=false, updatable=false)
		})
	private Destino destino;

	//bi-directional many-to-one association to Informe
	@ManyToOne
	@JoinColumn(name="ID_INFORME", insertable=false, updatable=false)
	private Informe informe;

	//bi-directional many-to-one association to Tcon1
	@OneToMany(mappedBy="snapshot")
	private Set<Tcon1> tcon1s;

	public Snapshot() {
	}

	public SnapshotPK getId() {
		return this.id;
	}

	public void setId(SnapshotPK id) {
		this.id = id;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Destino getDestino() {
		return this.destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public Informe getInforme() {
		return this.informe;
	}

	public void setInforme(Informe informe) {
		this.informe = informe;
	}

	public Set<Tcon1> getTcon1s() {
		return this.tcon1s;
	}

	public void setTcon1s(Set<Tcon1> tcon1s) {
		this.tcon1s = tcon1s;
	}

	public Tcon1 addTcon1(Tcon1 tcon1) {
		getTcon1s().add(tcon1);
		tcon1.setSnapshot(this);

		return tcon1;
	}

	public Tcon1 removeTcon1(Tcon1 tcon1) {
		getTcon1s().remove(tcon1);
		tcon1.setSnapshot(null);

		return tcon1;
	}

}