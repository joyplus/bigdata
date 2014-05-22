package tv.joyplus.backend.huan;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class LogTransforLauncher {
	private JobLauncher jobLauncher;
	private Job job;

	public void execute() {
		try {
			System.out.println("start");
			JobParameters jobParameters = 
					  new JobParametersBuilder()
					  .addLong("time",System.currentTimeMillis()).toJobParameters();
			jobLauncher.run(job, jobParameters);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	
}
