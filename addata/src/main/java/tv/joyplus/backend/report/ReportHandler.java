package tv.joyplus.backend.report;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;
import com.trendrr.beanstalk.BeanstalkJob;

import de.ailis.pherialize.MixedArray;
import de.ailis.pherialize.Pherialize;
import tv.joyplus.backend.report.task.ReportTask;


public class ReportHandler {
	
	protected static Log log = LogFactory.getLog(ReportHandler.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
		
		ReportTask reportTask = (ReportTask) ctx.getBean("reportTask");
		
		BeanstalkClient bsClient = new BeanstalkClient("localhost", 11300, "tubeCustomizeReport");
		
		try {
			while (true) {
				BeanstalkJob job = bsClient.reserve(1);
				String strJobText = new String(job.getData());
				MixedArray jobArr = Pherialize.unserialize(strJobText).toArray();
				strJobText = jobArr.toString();
				reportTask.processReport(strJobText);
				//bsClient.deleteJob(job);
			}
		} catch (BeanstalkException e) {
			e.printStackTrace();
		} finally {
			bsClient.close();
		}
	}

}
