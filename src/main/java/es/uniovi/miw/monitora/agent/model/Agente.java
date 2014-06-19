package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the AGENTE database table.
 * 
 */
@Entity
public class Agente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AGENTE_ID")
	private int agenteId;

	@Column(name="COMENTARIOS")
	private String comentarios;

	@Column(name="IP_AGENTE")
	private String ipAgente;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE")
	private Cliente cliente;

	//bi-directional many-to-many association to Destino
	@ManyToMany
	@JoinTable(
		name="AGENTE_DESTINO"
		, joinColumns={
			@JoinColumn(name="ID_AGENTE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_CLIENTE"),
			@JoinColumn(name="ID_DESTINO", referencedColumnName="ID_DESTINO")
			}
		)
	private Set<Destino> destinos;

	public Agente() {
	}

	public int getAgenteId() {
		return this.agenteId;
	}

	public void setAgenteId(int agenteId) {
		this.agenteId = agenteId;
	}

	public String getComentarios() {
		return this.comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	public String getIpAgente() {
		return this.ipAgente;
	}

	public void setIpAgente(String ipAgente) {
		this.ipAgente = ipAgente;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Set<Destino> getDestinos() {
		return this.destinos;
	}

	public void setDestinos(Set<Destino> destinos) {
		this.destinos = destinos;
	}

}