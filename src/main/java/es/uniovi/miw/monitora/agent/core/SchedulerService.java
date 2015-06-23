package es.uniovi.miw.monitora.agent.core;

import es.uniovi.miw.monitora.server.model.InfPlanDest;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public interface SchedulerService {

	void add(InfPlanDest infoPlanDes) throws BusinessException;

	void start() throws BusinessException;

	void stop() throws BusinessException;

	int size() throws BusinessException;

}
