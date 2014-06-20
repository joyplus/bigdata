package tv.joyplus.backend.huan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;

public class LogAnalyzerLauncher {
	private final static Log log = LogFactory.getLog(LogAnalyzerLauncher.class);
	private static JobParameters oldJobParameters = null;
	private JobRepository jobRepository;
	private JobLauncher jobLauncher;
	private Job job;

	public void execute() {
		try {
			log.info("job start");
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
			if(oldJobParameters == null) {
				log.info("first start");
				oldJobParameters = jobParameters;
				jobLauncher.run(job, jobParameters);
			}else{
				JobExecution jobExecution = jobRepository.getLastJobExecution(
						job.getName(), oldJobParameters);
				if (jobExecution == null || !jobExecution.getStatus().isRunning()) {
					log.info("previews job stopped, this job will start");
					oldJobParameters = jobParameters;
					jobLauncher.run(job, jobParameters);
				} else {
					log.info("previews job doesn't stopped, this job will exit");
				}
			}
			
			log.info("job done");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public void setJob(Job job) {
		this.job = job;
	}

}
