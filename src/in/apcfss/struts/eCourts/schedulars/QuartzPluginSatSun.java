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

public class QuartzPluginSatSun implements PlugIn {

	@Override
	public void destroy() {
		// null
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig config)
			throws ServletException {
		JobDetail job_CASESDATAUPDATE = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("HighCourtCasesDataSchedularJob", "group1").build();
		try {
			Trigger trigger_CASESDATAUPDATE = TriggerBuilder.newTrigger().withIdentity("casesdataupdateTrigger", "group1").withSchedule(CronScheduleBuilder.cronSchedule(
					"0 0/5 8-20 ? * SAT,SUN *")).build();//0 10 21 * 1-6 ?
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job_CASESDATAUPDATE, trigger_CASESDATAUPDATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}