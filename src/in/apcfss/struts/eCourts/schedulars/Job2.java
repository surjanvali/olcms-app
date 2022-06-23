package in.apcfss.struts.eCourts.schedulars;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Job2 implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		
		Date d1 = new Date();
		
		System.out.println("Print Print Struts 1.3 + Quartz 2.1.5 integration example ---JOB2 :"+d1);

	}
}
