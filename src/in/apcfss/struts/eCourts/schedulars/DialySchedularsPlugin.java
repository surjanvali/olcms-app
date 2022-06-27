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

public class DialySchedularsPlugin implements PlugIn {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(ActionServlet arg0, ModuleConfig arg1) throws ServletException {

		JobDetail job_SMSALERTS = JobBuilder.newJob(SMSAlertsJob.class).withIdentity("SMSAlertsJob", "group2").build();
		JobDetail job_CAUSELIST = JobBuilder.newJob(HighCourtCauseListSchedular.class).withIdentity("HighCourtCauseListSchedularJob", "group3").build();

		try {

			Trigger trigger_SMSALERTS = TriggerBuilder.newTrigger().withIdentity("smsalertsTrigger", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0 15 16 * 1-6 ?")).build();
			Trigger trigger_CAUSELIST = TriggerBuilder.newTrigger().withIdentity("causelistTrigger", "group3").withSchedule(CronScheduleBuilder.cronSchedule("0 30 21 * 1-6 ?")).build();//30 9 ? * MON-FRI

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job_SMSALERTS, trigger_SMSALERTS);
			scheduler.scheduleJob(job_CAUSELIST, trigger_CAUSELIST);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}