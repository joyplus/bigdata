package tv.joyplus.backend.huan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class LogTransforLauncher {
	private final static Log log = LogFactory.getLog(LogTransforLauncher.class);
	private JobLauncher jobLauncher;
	private Job job;

	public void execute() {
		try {
			log.debug("job start");
			JobParameters jobParameters = 
					  new JobParametersBuilder()
					  .addLong("time",System.currentTimeMillis()).toJobParameters();
			jobLauncher.run(job, jobParameters);
			log.debug("job done");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	
}
