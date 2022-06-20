package in.apcfss.struts.eCourts.schedulars;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzPlugin implements PlugIn {

	@Override
	public void destroy() {
		// null
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig config)
			throws ServletException {

		// JobDetail job = JobBuilder.newJob(SchedulerJob.class).withIdentity("anyJobName", "group1").build();
		JobDetail job_CASESDATAUPDATE = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("HighCourtCasesDataSchedularJob", "group1").build();
		// JobDetail job_SMSALERTS = JobBuilder.newJob(SMSAlertsJob.class).withIdentity("SMSAlertsJob", "group2").build();
		// JobDetail job_CAUSELIST = JobBuilder.newJob(HighCourtCauseListSchedular.class).withIdentity("HighCourtCauseListSchedularJob", "group3").build();

		try {

			/*Trigger trigger_CASESDATAUPDATE = TriggerBuilder.newTrigger().withIdentity("casesdataupdate", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
			Trigger trigger_SMSALERTS = TriggerBuilder.newTrigger().withIdentity("smsalerts", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0 16 * * 1,2,3,4,5,6 ?")).build();
			Trigger trigger_CAUSELIST = TriggerBuilder.newTrigger().withIdentity("causelist", "group3").withSchedule(CronScheduleBuilder.cronSchedule("0 21 * * 1,2,3,4,5,6 ?")).build();//30 9 ? * MON-FRI
			*/
			
			Trigger trigger_CASESDATAUPDATE = TriggerBuilder.newTrigger().withIdentity("casesdataupdateTrigger", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")).build();
			// Trigger trigger_SMSALERTS = TriggerBuilder.newTrigger().withIdentity("smsalertsTrigger", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0 0 16 * 1,2,3,4,5,6 ?")).build();
			// Trigger trigger_CAUSELIST = TriggerBuilder.newTrigger().withIdentity("causelistTrigger", "group3").withSchedule(CronScheduleBuilder.cronSchedule("0 0 21 * 1,2,3,4,5,6 ?")).build();//30 9 ? * MON-FRI

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job_CASESDATAUPDATE, trigger_CASESDATAUPDATE);
			// scheduler.scheduleJob(job_SMSALERTS, trigger_SMSALERTS);
			// scheduler.scheduleJob(job_CAUSELIST, trigger_CAUSELIST);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}