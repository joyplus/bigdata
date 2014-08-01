package tv.joyplus.backend.appinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import tv.joyplus.backend.config.ConfigManager;

public class AppLogLoadLauncher {
    private final static Log log = LogFactory.getLog(AppLogLoadLauncher.class);
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private Job job;
    @Autowired
    private ConfigManager configManager;
    public void execute() {
        try {
            //add by Jas@20140801 for Loginfo state
            log.info("AppLogLoadLauncher execute @"+ new Date(System.currentTimeMillis()));
            if(configManager.getBusinessIds().length>0){//it has assign businessid for this AppAnalyzer to analyzer
                jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
            }
            //add by Jas@20140801 for Loginfo state
            log.info("AppLogLoadLauncher execute done@"+ new Date(System.currentTimeMillis()));
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
