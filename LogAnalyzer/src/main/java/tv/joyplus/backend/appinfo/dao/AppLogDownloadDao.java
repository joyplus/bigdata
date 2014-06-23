package tv.joyplus.backend.appinfo.dao;

import java.util.List;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;

public interface AppLogDownloadDao {
	public AppLogDownloadInfo get();
	public List<String> listAllIdent();
	public List<AppLogDownloadInfo> list();
	public void save(AppLogDownloadInfo instance);
	public void batchSave(final List<? extends AppLogDownloadInfo> list);
	public int updateStatus(long id, int status);
	public int updateStatus(AppLogDownloadInfo instance);
	public boolean existIdent(String ident);
}
