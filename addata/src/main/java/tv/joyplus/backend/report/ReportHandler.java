package tv.joyplus.backend.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.Pherialize;
import tv.joyplus.backend.report.task.ReportTask;


public class ReportHandler {
	
	protected static Log log = LogFactory.getLog(ReportHandler.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
		ReportTask reportTask = (ReportTask) ctx.getBean("reportTask");
		BeanstalkClient bsClient = (BeanstalkClient) ctx.getBean("bsClient");
		
		while (true) {
			try {
				BeanstalkJob job = bsClient.reserve(1);
				if (job == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					continue;
				}
				String strJobText = new String(job.getData());
				Mixed jobArr = Pherialize.unserialize(strJobText);
				strJobText = jobArr.toString();
				reportTask.processReport(strJobText);
				bsClient.deleteJob(job);
			} catch (Exception e) {
//				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
	}

}
