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
		JobDetail job = JobBuilder.newJob(HighCourtCasesDataSchedular.class).withIdentity("HighCourtCasesDataSchedular", "group2").build();

		try {

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("anyTriggerName", "group1").withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * *")).build();

			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
			
			/*Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("anyTriggerName", "group2").withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ?")).build();

			Scheduler scheduler2 = new StdSchedulerFactory().getScheduler();
			scheduler2.start();
			scheduler2.scheduleJob(job2, trigger2);
			*/

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
