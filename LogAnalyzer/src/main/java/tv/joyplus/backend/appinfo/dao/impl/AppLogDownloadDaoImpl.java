package tv.joyplus.backend.appinfo.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;

public class AppLogDownloadDaoImpl extends JdbcDaoSupport implements
		AppLogDownloadDao {
	

	@Override
	public List<String> listAllIdent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppLogDownloadInfo> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AppLogDownloadInfo> listUnzip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(AppLogDownloadInfo instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateZip(AppLogDownloadInfo instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateZip(long id, long status) {
		// TODO Auto-generated method stub

	}

}
