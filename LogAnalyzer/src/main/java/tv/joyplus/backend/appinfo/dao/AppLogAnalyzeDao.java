package tv.joyplus.backend.appinfo.dao;

import java.util.List;

import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;

public interface AppLogAnalyzeDao {
	public void save(AppLogAnalyzeInfo instance);
	public List<AppLogAnalyzeInfo> list();
	public List<AppLogAnalyzeInfo> listUnAnalyzed();
	public void updateStatus(long id, int status);
	public void updateStatus(AppLogAnalyzeInfo instance);
}
