package tv.joyplus.backend.huan.dao;

import com.trendrr.beanstalk.BeanstalkClient;
import com.trendrr.beanstalk.BeanstalkJob;
import de.ailis.pherialize.Pherialize;
//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"classpath:huan/batch.xml"})
//@RunWith(SpringJUnit4ClassRunner.class)
public class LogProcessDaoTest {
	@Autowired
	private LogProcessDao logProcessDao;
	@Autowired
	private BeanstalkClient beanstalk;
//	@Test
	public void batch() throws Exception {
//		List<Map> list = new ArrayList<Map>();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("equipment_sn", "");
//		map.put("equipment_key", "123");
//		map.put("device_name", "asd");
//		map.put("user_pattern", 0);
//		map.put("date", "2014-05-20 10:18:26");
//		map.put("operation_type", "003");
//		map.put("operation_extra", "");
//		map.put("publication_id", 28);
//		map.put("zone_id", 47);
//		map.put("campaign_id", 235);
//		map.put("creative_id", 1571);
//		map.put("client_ip", "127.0.0.1");
//		map.put("business_id", "MDADV");
//		list.add(map);
//		logProcessDao.sendToRequestLog(list);
//		beanstalk.watchTube("MDADV_REQUEST_DEVICE_LOG");
//		BeanstalkJob job = beanstalk.reserve(null);
//		beanstalk.deleteJob(job);
//		assertEquals(Pherialize.serialize(Pherialize.serialize(map)), new String(job.getData()));
	}
}
