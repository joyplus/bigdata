package tv.joyplus.backend.appinfo.dao;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;

import java.util.List;

public interface AppLogDownloadDao {
	public AppLogDownloadInfo get();
	public List<String> listAllIdent();
	public List<AppLogDownloadInfo> list();
	public void save(AppLogDownloadInfo instance);
	public void batchSave(final List<? extends AppLogDownloadInfo> list);
	public int updateStatus(long id, int status);
	public int updateStatus(AppLogDownloadInfo instance);
	public boolean existIdent(String ident, String businessId);
}
