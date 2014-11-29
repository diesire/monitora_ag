package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * The persistent class for the CONSULTA database table.
 * 
 */
@Entity
public class Consulta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CONS_ID")
	private int consId;

	@Column(name="COMANDO_SO")
	private String comandoSo;

	@Column(name="DESC_CORTA")
	private String descCorta;

	@Column(name="DESC_LARGA")
	private String descLarga;

	@Temporal(TemporalType.DATE)
	@Column(name="F_ULTIMA_MODIFICACION")
	private Date fUltimaModificacion;

	@Column(name="SQL_CREATE")
	private String sqlCreate;

	@Column(name="SQL_SELECT")
	private String sqlSelect;
	
	@Column(name="TABLA")
	private String tabla;

	@Column(name="TIPO")
	private String tipo;

	//bi-directional many-to-many association to TipoDestino
	@ManyToMany
	@JoinTable(
		name="CONSULTA_TIPO_DESTINO"
		, joinColumns={
			@JoinColumn(name="ID_CONSULTA")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_TIPO_DESTINO")
			}
		)
	private Set<TipoDestino> tipoDestinos = new HashSet<TipoDestino>();

	//bi-directional many-to-one association to InformeConsulta
	@OneToMany(mappedBy="consulta")
	private Set<InformeConsulta> informeConsultas = new HashSet<InformeConsulta>();

	//bi-directional many-to-one association to Tcon1
	@OneToMany(mappedBy="consulta")
	private Set<Tcon1> tcon1s;

	public Consulta() {
	}

	public int getConsId() {
		return this.consId;
	}

	public void setConsId(int consId) {
		this.consId = consId;
	}

	public String getComandoSo() {
		return this.comandoSo;
	}

	public void setComandoSo(String comandoSo) {
		this.comandoSo = comandoSo;
	}

	public String getDescCorta() {
		return this.descCorta;
	}

	public void setDescCorta(String descCorta) {
		this.descCorta = descCorta;
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

	public String getSqlCreate() {
		return this.sqlCreate;
	}

	public void setSqlCreate(String sqlCreate) {
		this.sqlCreate = sqlCreate;
	}

	public String getSqlSelect() {
		return this.sqlSelect;
	}

	public void setSqlSelect(String sqlSelect) {
		this.sqlSelect = sqlSelect;
	}

	public String getTabla() {
		return this.tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Set<TipoDestino> getTipoDestinos() {
		return this.tipoDestinos;
	}

	public void setTipoDestinos(Set<TipoDestino> tipoDestinos) {
		this.tipoDestinos = tipoDestinos;
	}
	
	public TipoDestino addTipoDestino(TipoDestino tipoDestino) {
		getTipoDestinos().add(tipoDestino);
		
		return tipoDestino;
	}
	
	public TipoDestino removeTipoDestino(TipoDestino tipoDestino) {
		getTipoDestinos().remove(tipoDestino);
		
		return tipoDestino;
	}

	public Set<InformeConsulta> getInformeConsultas() {
		return this.informeConsultas;
	}

	public void setInformeConsultas(Set<InformeConsulta> informeConsultas) {
		this.informeConsultas = informeConsultas;
	}

	public InformeConsulta addInformeConsulta(InformeConsulta informeConsulta) {
		getInformeConsultas().add(informeConsulta);
		informeConsulta.setConsulta(this);

		return informeConsulta;
	}

	public InformeConsulta removeInformeConsulta(InformeConsulta informeConsulta) {
		getInformeConsultas().remove(informeConsulta);
		informeConsulta.setConsulta(null);

		return informeConsulta;
	}

	public Set<Tcon1> getTcon1s() {
		return this.tcon1s;
	}

	public void setTcon1s(Set<Tcon1> tcon1s) {
		this.tcon1s = tcon1s;
	}

	public Tcon1 addTcon1(Tcon1 tcon1) {
		getTcon1s().add(tcon1);
		tcon1.setConsulta(this);

		return tcon1;
	}

	public Tcon1 removeTcon1(Tcon1 tcon1) {
		getTcon1s().remove(tcon1);
		tcon1.setConsulta(null);

		return tcon1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + consId;
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
		Consulta other = (Consulta) obj;
		if (consId != other.consId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Consulta [consId=").append(consId)
				.append(", comandoSo=").append(comandoSo)
				.append(", descCorta=").append(descCorta)
				.append(", descLarga=").append(descLarga)
				.append(", fUltimaModificacion=").append(fUltimaModificacion)
				.append(", sqlCreate=").append(sqlCreate)
				.append(", sqlSelect=").append(sqlSelect).append(", tabla=")
				.append(tabla).append(", tipo=").append(tipo).append("]");
		return builder.toString();
	}
	
	

}