package tv.joyplus.backend.appinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
	public static final Log log = LogFactory.getLog(Launcher.class);
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:appinfo/batch.xml");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
        Job job = (Job) ctx.getBean("appLogDownloadJob");
        try {
            jobLauncher.run(job, new JobParameters());
        }catch (Exception e){
            log.error(e.getMessage());
        }
	}
}
