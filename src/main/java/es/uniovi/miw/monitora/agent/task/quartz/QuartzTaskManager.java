package es.uniovi.miw.monitora.agent.task.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uniovi.miw.monitora.agent.core.SchedulerService;
import es.uniovi.miw.monitora.server.model.Cliente;
import es.uniovi.miw.monitora.server.model.Consulta;
import es.uniovi.miw.monitora.server.model.Destino;
import es.uniovi.miw.monitora.server.model.InfPlanDest;
import es.uniovi.miw.monitora.server.model.Informe;
import es.uniovi.miw.monitora.server.model.InformeConsulta;
import es.uniovi.miw.monitora.server.model.LineaCron;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class QuartzTaskManager implements SchedulerService {

	static Logger logger = LoggerFactory.getLogger(QuartzTaskManager.class);
	private static QuartzTaskManager instance;
	private Scheduler scheduler;
	private JobListener jobListener;

	public QuartzTaskManager() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
	}

	@Override
	public void start() throws BusinessException {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public void stop() throws BusinessException {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new BusinessException(e);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public int size() throws BusinessException {
		int i = 0;
		try {
			for (String group : scheduler.getJobGroupNames()) {
				// enumerate each job in group
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher
						.<JobKey> groupEquals(group))) {
					i++;
				}
			}
		} catch (SchedulerException e) {
			throw new BusinessException(e);
		}
		return i;
	}

	public JobListener getJobListener() {
		return jobListener;
	}

	public void setJobListener(JobListener jobListener)
			throws BusinessException {
		this.jobListener = jobListener;
		try {
			scheduler.getListenerManager().addJobListener(jobListener,
					GroupMatcher.<JobKey> anyGroup());
		} catch (SchedulerException e) {
			throw new BusinessException(e);
		}
	}

	public static QuartzTaskManager getInstance() throws BusinessException {
		if (instance == null) {
			try {
				instance = new QuartzTaskManager();
			} catch (SchedulerException e) {
				throw new BusinessException(e);
			}
		}
		return instance;
	}

	@Override
	public void add(InfPlanDest infoPlanDes) throws BusinessException {
		String jobName;
		String groupName = getGroupName(infoPlanDes);
		String triggerName;

		Set<Trigger> lineas = new HashSet<Trigger>();
		Set<JobDetail> jobs = new HashSet<JobDetail>();

		for (LineaCron cron : infoPlanDes.getPlanificacion().getLineaCrons()) {
			triggerName = getTriggerName(cron);
			lineas.add(createTrigger(groupName, triggerName, cron));
		}

		for (InformeConsulta infoCon : infoPlanDes.getInforme()
				.getInformeConsultas()) {
			jobName = getJobName(infoCon.getConsulta());
			jobs.add(createJob(groupName, jobName, infoPlanDes.getDestino(),
					infoCon.getInforme(), infoCon.getConsulta()));
		}

		try {
			for (Trigger trigger : lineas) {
				for (JobDetail job : jobs) {
					scheduler.scheduleJob(job, trigger);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	private String getJobName(Consulta consulta) {
		return MessageFormat.format("Job-{0}", consulta.getConsId());
	}

	private String getTriggerName(LineaCron cron) {
		return MessageFormat.format("Trigger-{0}", cron.getIdLineaCron());
	}

	private String getGroupName(InfPlanDest infoPlanDes) {
		return MessageFormat.format("Group-{0}-{1}-{2}", infoPlanDes.getId()
				.getIdDestino(), infoPlanDes.getId().getIdInforme(),
				infoPlanDes.getPlanificacion().getIdPlan());
	}

	private JobDetail createJob(String groupName, String jobName,
			Destino destino, Informe informe, Consulta consulta)
			throws BusinessException {
		switch (consulta.getTipo()) {
		case Consulta.TIPO_BASE_DATOS:
			return newJob(ConsultaDBJob.class)
					.withIdentity(jobName, groupName)
					.usingJobData("tabla", consulta.getTabla())
					.usingJobData("sql_create", consulta.getSqlCreate())
					.usingJobData("sql_select", consulta.getSqlSelect())
					.usingJobData("sql_insert", consulta.getSqlInsert())
					.usingJobData("idCliente",
							destino.getCliente().getIdCliente())
					.usingJobData("idDestino", destino.getId().getIdDestino())
					.usingJobData("idInforme", informe.getInfoId())
					.usingJobData("idConsulta", consulta.getConsId()).build();
		case Consulta.TIPO_SISTEMA_OPERATIVO:
			return newJob(ConsultaSOJob.class)
					.withIdentity(jobName, groupName)
					.usingJobData("tabla", consulta.getTabla())
					.usingJobData("sql_create", consulta.getSqlCreate())
					.usingJobData("comando_so", consulta.getSqlSelect())
					.usingJobData("sql_insert", consulta.getSqlInsert())
					.usingJobData("idCliente",
							destino.getCliente().getIdCliente())
					.usingJobData("idDestino", destino.getId().getIdDestino())
					.usingJobData("idInforme", informe.getInfoId())
					.usingJobData("idConsulta", consulta.getConsId()).build();
		default:
			throw new BusinessException("Invalid Consulta.tipo");
		}
	}

	private CronTrigger createTrigger(String groupName, String triggerName,
			LineaCron cron) {
		return newTrigger()
				.withIdentity(triggerName, groupName)
				.startNow()
				.withSchedule(
						CronScheduleBuilder.cronSchedule(cron.getExpresion()))
				.build();
	}

}
