package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.LogInfo;

public interface LogDataDao {
	public void batchLogData(List<? extends LogInfo> list);
}
