package tv.joyplus.backend.huan.core;

import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.dao.LogDataDao;
import tv.joyplus.backend.huan.dao.LogInfoDao;

public class LogItemWriter implements ItemWriter<LogInfo> {
	@Autowired
	private LogDataDao logDataDao;
	@Autowired
	private LogInfoDao logInfoDao;
	@Override
	public void write(List<? extends LogInfo> list) throws SQLException {
		logDataDao.batchLogData(list);
		logInfoDao.batchLogInfo(list);
		System.out.println("write done!");
	}
}
