package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the CLIENTE database table.
 * 
 */
@Entity
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_CLIENTE")
	private int idCliente;

	@Column(name = "LOGO")
	private String logo;

	@Column(name = "NOMBRE")
	private String nombre;

	// bi-directional many-to-one association to Agente
	@OneToMany(mappedBy = "cliente", orphanRemoval = true)
	private Set<Agente> agentes = new HashSet<Agente>();

	// bi-directional many-to-one association to Destino
	@OneToMany(mappedBy = "cliente", orphanRemoval = true)
	private Set<Destino> destinos = new HashSet<Destino>();

	public Cliente() {
	}

	public int getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Agente> getAgentes() {
		return this.agentes;
	}

	public void setAgentes(Set<Agente> agentes) {
		this.agentes = agentes;
	}

	public Agente addAgente(Agente agente) {
		agente.setCliente(this);
		getAgentes().add(agente);

		return agente;
	}

	public Agente removeAgente(Agente agente) {
		getAgentes().remove(agente);
		agente.setCliente(null);

		return agente;
	}

	public Set<Destino> getDestinos() {
		return this.destinos;
	}

	public void setDestinos(Set<Destino> destinos) {
		this.destinos = destinos;
	}

	public Destino addDestino(Destino destino) {
		getDestinos().add(destino);
		destino.setCliente(this);

		return destino;
	}

	public Destino removeDestino(Destino destino) {
		destino.setCliente(null);
		getDestinos().remove(destino);

		return destino;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCliente;
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
		Cliente other = (Cliente) obj;
		if (idCliente != other.idCliente)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cliente [idCliente=").append(idCliente)
				.append(", logo=").append(logo).append(", nombre=")
				.append(nombre).append("]");
		return builder.toString();
	}

}