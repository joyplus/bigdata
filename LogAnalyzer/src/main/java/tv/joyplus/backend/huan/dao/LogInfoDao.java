package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.LogData;
import tv.joyplus.backend.huan.beans.LogInfo;

public interface LogInfoDao {
	public void batchLogInfo(List<? extends LogInfo> list);
	public List<LogInfo> findLogInfo();
	public void updateMaxId(LogData data);
	public long getCampaignId(long creativeId);
	public long getPublicationId(long zoneId);
}
