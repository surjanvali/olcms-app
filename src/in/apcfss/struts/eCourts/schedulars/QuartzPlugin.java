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
		JobDetail job_CASESDATAUPDATE = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("HighCourtCasesDataSchedular", "group1").build();
		JobDetail job_SMSALERTS = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("SMSAlertsJob", "group2").build();
		JobDetail job_CAUSELIST = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("HighCourtCasesDataSchedular", "group3").build();

		try {

			Trigger trigger_CASESDATAUPDATE = TriggerBuilder.newTrigger().withIdentity("CASESDATAUPDATE", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
			Trigger trigger_SMSALERTS = TriggerBuilder.newTrigger().withIdentity("SMSALERTS", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0 18 * * * ?")).build();
			Trigger trigger_CAUSELIST = TriggerBuilder.newTrigger().withIdentity("CAUSELIST", "group3").withSchedule(CronScheduleBuilder.cronSchedule("0 21 ? * SUN-THU")).build();//30 9 ? * MON-FRI

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job_CASESDATAUPDATE, trigger_CASESDATAUPDATE);
			scheduler.scheduleJob(job_SMSALERTS, trigger_SMSALERTS);
			scheduler.scheduleJob(job_CAUSELIST, trigger_CAUSELIST);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}