package tv.joyplus.backend.appinfo.dao;

import java.util.List;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;

public interface AppLogDownloadDao {
	public List<String> listAllIdent();
	public List<AppLogDownloadInfo> list();
	public List<AppLogDownloadInfo> listUnzip();
	public void save(AppLogDownloadInfo instance);
	public void updateZip(AppLogDownloadInfo instance);
	public void updateZip(long id, long status);
}
