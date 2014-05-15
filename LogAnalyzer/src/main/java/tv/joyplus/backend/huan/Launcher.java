package tv.joyplus.backend.huan;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:huan/batch.xml");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job job = (Job) ctx.getBean("logAnalyzerJob");
		try {
			JobExecution result = jobLauncher.run(job, new JobParameters());
			System.out.println(result.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
