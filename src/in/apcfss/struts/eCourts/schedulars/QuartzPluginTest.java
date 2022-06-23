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

public class QuartzPluginTest implements PlugIn {

	@Override
	public void destroy() {
		// null
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig config)
			throws ServletException {

		//JobDetail job = JobBuilder.newJob(SchedulerJob.class).withIdentity("anyJobName", "group1").build();
		JobDetail job2 = JobBuilder.newJob(Job2.class).withIdentity("anyJobName2", "group2").build();
		
		try {

			//Trigger trigger = TriggerBuilder.newTrigger().withIdentity("causelistTrigger", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * 1-6 ?")).build();
			Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("causelistTrigger2", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * 1-6 ?")).build();
			
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			//scheduler.scheduleJob(job, trigger);
			scheduler.scheduleJob(job2, trigger2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}