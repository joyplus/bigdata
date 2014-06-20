package tv.joyplus.backend.appinfo.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;

public class AppLogDownloadDaoImpl extends JdbcDaoSupport implements
		AppLogDownloadDao {
	

	@Override
	public List<String> listAllIdent() {
		String sql = "SELECT ident FROM " + AppLogDownloadInfo.TableName();
		return getJdbcTemplate().queryForList(sql, String.class);
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
		String sql = "INSERT INTO " + AppLogDownloadInfo.TableName() + "(ident,url,path,filename,"
				+ "mime_type,size,put_time,status,create_time) VALUES(?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, new Object[]{instance.getIdent(), instance.getUrl(), 
				instance.getPath(), instance.getFilename(), instance.getMimeType(), instance.getSize(),
				instance.getPutTime(), 0, instance.getCreateTime()});
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
