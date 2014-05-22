package tv.joyplus.backend.huan.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.LogData;
import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.dao.LogDataDao;
import tv.joyplus.backend.huan.dao.LogInfoDao;
import tv.joyplus.backend.huan.dao.LogProcessDao;

public class LogTransforTasklet implements Tasklet {
	private final static Log log = LogFactory.getLog(LogTransforTasklet.class);
	
	@Autowired
	private LogDataDao logDataDao;
	@Autowired
	private LogInfoDao logInfoDao;
	@Autowired
	private LogProcessDao logProcessDao;
	private String businessId;
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) {
		
		List<LogInfo> list = logInfoDao.findLogInfo();
		log.debug("find loginfos :" + list.size());
		
		List<Map> processes = new ArrayList<Map>();
		for(LogInfo info : list) {
			long campaignId = logInfoDao.getCampaignId(info.getCreativeId());
			List<LogData> datas = logDataDao.find(info);
			//更新统计过的最大id
			updateInfoMaxId(datas);
			for(LogData data : datas) {
				long publicationId = logInfoDao.getPublicationId(data.getZoneId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("equipment_sn", "");
				map.put("equipment_key", data.getEquipmentKey());
				map.put("device_name", data.getDeviceName());
				map.put("user_pattern", "");
				map.put("date", data.getAdDate());
				map.put("operation_type", "003");
				map.put("operation_extra", "");
				map.put("publication_id", publicationId);
				map.put("zone_id", data.getZoneId());
				map.put("campaign_id", campaignId);
				map.put("creative_id", info.getCreativeId());
				map.put("client_ip", data.getIp());
				map.put("business_id", businessId);
				processes.add(map);
			}
		}
		logProcessDao.batch(processes);
		return RepeatStatus.FINISHED;
	}
	
	private void updateInfoMaxId(List<LogData> list) {
		if(list==null || list.size()<1)
			return ;
		LogData data = list.get(0);
		logInfoDao.updateMaxId(data);
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
}
