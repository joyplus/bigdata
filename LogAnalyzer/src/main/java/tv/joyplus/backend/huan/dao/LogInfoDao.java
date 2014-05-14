package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.LogInfo;

public interface LogInfoDao {
	public void batchLogInfo(List<? extends LogInfo> list);
}
