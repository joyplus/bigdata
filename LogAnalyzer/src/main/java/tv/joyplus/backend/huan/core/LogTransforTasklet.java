package tv.joyplus.backend.huan.core;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.beans.LogProcess;
import tv.joyplus.backend.huan.dao.LogDataDao;
import tv.joyplus.backend.huan.dao.LogInfoDao;
import tv.joyplus.backend.huan.dao.LogProcessDao;

public class LogTransforTasklet implements Tasklet {
	private final static Log log = LogFactory.getLog(LogTransforTasklet.class);
	
	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Autowired
	private LogDataDao logDataDao;
	@Autowired
	private LogInfoDao logInfoDao;
	@Autowired
	private LogProcessDao logProcessDao;
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		List<LogInfo> list = logInfoDao.findLogInfo();
		log.debug("find loginfos :" + list.size());
		
		List<LogProcess> processes = new ArrayList<LogProcess>();
		for(LogInfo info : list) {
			long campaignId = logInfoDao.getCampaignId(info.getCreativeId());
			List<LogInfo> datas = logDataDao.find(info);
			for(LogInfo data : datas) {
				long publicationId = logInfoDao.getPublicationId(data.getZoneId());
				LogProcess logProcess = new LogProcess();
				logProcess.setAdDate(formatDate(data.getAdDate()));
				logProcess.setCampaignId(campaignId);
				logProcess.setClientIp(data.getIp());
				logProcess.setCreativeId(info.getCreativeId());
				logProcess.setDatetime(formatTimestamp(data.getAdDate()));
				logProcess.setDeviceName(data.getDeviceName());
				logProcess.setEquipmentKey(data.getEquitpmentKey());
				logProcess.setPublicationId(publicationId);
				logProcess.setZoneId(data.getZoneId());
				processes.add(logProcess);
			}
		}
		logProcessDao.batch(processes);
		return RepeatStatus.FINISHED;
	}
	private Date formatDate(String str) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			java.util.Date date = df.parse(str);
			return new Date(date.getTime());
		} catch (ParseException e) {
			log.error(e);
		}
		return null;
	}
	private Timestamp formatTimestamp(String str) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			java.util.Date date = df.parse(str);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			log.error(e);
		}
		return null;
	}
}
