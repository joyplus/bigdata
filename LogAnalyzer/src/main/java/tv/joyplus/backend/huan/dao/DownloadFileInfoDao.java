package tv.joyplus.backend.huan.dao;

import java.util.List;

import tv.joyplus.backend.huan.beans.DownloadFileInfo;

public interface DownloadFileInfoDao {
	public List<String> listAllIdent();
	public List<DownloadFileInfo> listAll();
	public List<DownloadFileInfo> listUnzip();
	public void save(DownloadFileInfo instance);
	public void updateZip(DownloadFileInfo instance);
}
