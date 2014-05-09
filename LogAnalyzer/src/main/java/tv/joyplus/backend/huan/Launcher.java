package tv.joyplus.backend.huan;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Launcher {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:batch/batch.xml");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job job = (Job) ctx.getBean("logJob");
		Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        //jobParametersMap.put("input.file", new JobParameter("file:/Users/zino/Downloads/info.log"));
        //jobParametersMap.put("commit.num", new JobParameter("10"));
        jobParametersMap.put("input.file", new JobParameter("classpath:batch/info.log"));
		try {
			JobExecution result = jobLauncher.run(job, new JobParameters(jobParametersMap));
			System.out.println(result.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
