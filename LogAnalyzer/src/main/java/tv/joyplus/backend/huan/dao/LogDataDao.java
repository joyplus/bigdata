package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.LogData;
import tv.joyplus.backend.huan.beans.LogInfo;

public interface LogDataDao {
	public void batchLogData(List<? extends LogData> list);
	public List<LogData> find(LogInfo info);
}
