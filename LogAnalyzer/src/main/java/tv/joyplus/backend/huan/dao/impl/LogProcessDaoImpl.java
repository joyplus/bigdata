package tv.joyplus.backend.huan.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;

import tv.joyplus.backend.huan.beans.LogProcess;
import tv.joyplus.backend.huan.dao.LogProcessDao;

public class LogProcessDaoImpl extends JdbcDaoSupport implements LogProcessDao {

	private final static Log log = LogFactory.getLog(LogProcessDaoImpl.class);
	private String tubeName;
	@Autowired
	private BeanstalkClient beanstalk;
	@Override
	public void batch(List<LogProcess> list) {
		try {
			beanstalk.useTube(tubeName);
			for(LogProcess p : list) {
				beanstalk.put(1L, 0, 120, p.getDeviceName().getBytes());
			}
		} catch (BeanstalkException e) {
			e.printStackTrace();
		}
	}
	public void setTubeName(String tubeName) {
		this.tubeName = tubeName;
	}
	
}
