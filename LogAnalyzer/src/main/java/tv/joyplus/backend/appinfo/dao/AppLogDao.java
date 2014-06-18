package tv.joyplus.backend.appinfo.dao;

import java.util.List;

import tv.joyplus.backend.appinfo.beans.AppLogInfo;

public interface AppLogDao {
	public void save(AppLogInfo instance);
	public void batchSave(List<AppLogInfo> list);
}
