package in.apcfss.struts.eCourts.schedulars;
 
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
 
public class SchedulerJob implements Job
{
	public void execute(JobExecutionContext context)
	throws JobExecutionException {
 
		System.out.println("Print Print Struts 1.3 + Quartz 2.1.5 integration example ~");
		
	}
}
