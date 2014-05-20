package tv.joyplus.backend.huan.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.LogData;
import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.dao.LogDataDao;
import tv.joyplus.backend.huan.dao.LogInfoDao;

public class LogItemWriter implements ItemWriter<LogData> {
	private static final Log log = LogFactory.getLog(LogItemWriter.class);
	@Autowired
	private LogDataDao logDataDao;
	@Autowired
	private LogInfoDao logInfoDao;
	@Override
	public void write(List<? extends LogData> list) throws SQLException {
		logDataDao.batchLogData(list);
		
		List<LogInfo> infos = new ArrayList<LogInfo>();
		for(LogData d : list) {
			LogInfo i = new LogInfo();
			i.setAdurl(d.getAdurl());
			i.setCreativeId(0);
			i.setImgurl(d.getImgurl());
			i.setMaxId(0);
			i.setSid(d.getSid());
			i.setTitle(d.getTitle());
			infos.add(i);
		}
		logInfoDao.batchLogInfo(infos);
		log.debug("write done!");
	}
}
