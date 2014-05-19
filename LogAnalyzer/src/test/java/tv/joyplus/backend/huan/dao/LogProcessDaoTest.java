package tv.joyplus.backend.huan.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tv.joyplus.backend.huan.beans.LogProcess;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkJob;

@ContextConfiguration(locations = {"classpath*:huan/batch.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LogProcessDaoTest {
	@Autowired
	private LogProcessDao logProcessDao;
	@Autowired
	private BeanstalkClient beanstalk;
	@Test
	public void batch() throws Exception {
		List<LogProcess> list = new ArrayList<LogProcess>();
		LogProcess p = new LogProcess();
		p.setDeviceName("deviceName");
		list.add(p);
		logProcessDao.batch(list);
		beanstalk.watchTube("MDADV_REQUEST_DEVICE_LOG");
		BeanstalkJob job = beanstalk.reserve(null);
		beanstalk.deleteJob(job);
		assertEquals(p.getDeviceName(), new String(job.getData()));
	}

}
