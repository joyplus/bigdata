package tv.joyplus.backend.report;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tv.joyplus.backend.report.task.ReportTask;


public class ReportHandler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
		
		ReportTask reportTask = (ReportTask) ctx.getBean("reportTask");
		
		reportTask.queryData(null);
		
		while (true) {
			
			
			
		}
		
	
	}

}
