package tv.joyplus.backend.huan.core;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class LogUnzipTaskletTest {

	@Test
	public void execute() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:huan/batch.xml");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job job = (Job) ctx.getBean("logLoadJob");
		//Job job = (Job) ctx.getBean("logAnalyzerJob");
		try {
			JobExecution result = jobLauncher.run(job, new JobParameters());
//			assertEquals(BatchStatus.COMPLETED, result.getStatus());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
