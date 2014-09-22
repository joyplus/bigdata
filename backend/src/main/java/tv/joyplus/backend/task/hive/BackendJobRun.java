package tv.joyplus.backend.task.hive;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tv.joyplus.backend.task.hive.hibernate.util.HibernateUtil;

public class BackendJobRun {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length>0){
			try{
				ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");
				Task task = (Task) ctx.getBean(args[0]);
				HibernateUtil.initSessionFactory();
				if(task!=null){
					if(args.length>1){
						task.handle(args[1]);
					}else{
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						String date = format.format(new Date());
						task.handle(date);
					}
				}
				HibernateUtil.closeFactory();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}else{
			throw new Exception("have no task name");
		}
		
	}
	
}
