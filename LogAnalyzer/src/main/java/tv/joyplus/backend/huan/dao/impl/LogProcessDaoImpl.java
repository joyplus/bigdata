package tv.joyplus.backend.huan.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkException;

import de.ailis.pherialize.Mixed;
import de.ailis.pherialize.Pherialize;
import tv.joyplus.backend.exception.TaskException;
import tv.joyplus.backend.huan.beans.LogProcess;
import tv.joyplus.backend.huan.dao.LogProcessDao;

public class LogProcessDaoImpl extends JdbcDaoSupport implements LogProcessDao {

	private final static Log log = LogFactory.getLog(LogProcessDaoImpl.class);
	private String tubeName;
	@Autowired
	private BeanstalkClient beanstalk;
	@Override
	public void batch(List<Map> list) {
		try {
			beanstalk.useTube(tubeName);
			for(Map p : list) {
				String serialize = Pherialize.serialize(p);
				byte[] data = Pherialize.serialize(serialize).getBytes();
				log.debug(new String(data));
				beanstalk.put(1L, 0, 120, data);
			}
		} catch (BeanstalkException e) {
			throw new TaskException("");
		}
	}
	public void setTubeName(String tubeName) {
		this.tubeName = tubeName;
	}
}
