package es.uniovi.miw.monitora.agent.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import es.uniovi.miw.monitora.agent.model.keys.InfPlanDestBorradoPK;


/**
 * The persistent class for the INF_PLAN_DEST_BORRADO database table.
 * 
 */
@Entity
@Table(name="INF_PLAN_DEST_BORRADO")
public class InfPlanDestBorrado implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InfPlanDestBorradoPK id;

	public InfPlanDestBorrado() {
	}

	public InfPlanDestBorradoPK getId() {
		return this.id;
	}

	public void setId(InfPlanDestBorradoPK id) {
		this.id = id;
	}

}