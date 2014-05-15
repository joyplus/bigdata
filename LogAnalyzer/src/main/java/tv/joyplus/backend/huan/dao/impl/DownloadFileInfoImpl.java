package tv.joyplus.backend.huan.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.huan.beans.DownloadFileInfo;
import tv.joyplus.backend.huan.dao.DownloadFileInfoDao;

public class DownloadFileInfoImpl extends JdbcDaoSupport implements DownloadFileInfoDao {

	
	@Override
	public List<String> listAllIdent() {
		String sql = "SELECT ident FROM " + DownloadFileInfo.TableName();
		return getJdbcTemplate().queryForList(sql, String.class);
	}

	@Override
	public List<DownloadFileInfo> listAll() {
		String sql = "SELECT * FROM " + DownloadFileInfo.TableName();
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(DownloadFileInfo.class));
	}

	@Override
	public List<DownloadFileInfo> listUnzip() {
		String sql = "SELECT id,ident,url,path,filename,mime_type,zip,status,create_time FROM "+DownloadFileInfo.TableName()+" WHERE zip=0";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper(DownloadFileInfo.class));
	}

	@Override
	public void save(DownloadFileInfo instance) {
		String sql = "INSERT INTO "+DownloadFileInfo.TableName()+"(ident,url,path,filename,mime_type,zip,status,create_time)"
				+ "VALUES(?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, new Object[]{instance.getIdent(), instance.getUrl(), 
				instance.getPath(), instance.getFilename(),instance.getMimeType(), 0, 0, instance.getCreateTime()});
	}

	@Override
	public void updateZip(DownloadFileInfo instance) {
		String sql = "UPDATE "+DownloadFileInfo.TableName()+" SET zip=1 WHERE id=?";
		getJdbcTemplate().update(sql, new Object[]{instance.getId()});
	}
	
	
}
