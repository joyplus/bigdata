package tv.joyplus.backend.appinfo.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AppLogDownloadDaoImpl extends JdbcDaoSupport implements
		AppLogDownloadDao {
	private final BeanPropertyRowMapper<AppLogDownloadInfo> mapper = new BeanPropertyRowMapper<AppLogDownloadInfo>(AppLogDownloadInfo.class);

	@Override
	public AppLogDownloadInfo get() {
		String sql = "SELECT * FROM " + AppLogDownloadInfo.TableName() + " WHERE status=0 LIMIT 1";
		List<AppLogDownloadInfo> list = getJdbcTemplate().query(sql, mapper);
		if(list==null || list.size()<=0) {
			return null;
		}
		AppLogDownloadInfo info = list.iterator().next();
		list = null;
		return info;
	}

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
	public void save(AppLogDownloadInfo instance) {
		String sql = "INSERT INTO " + AppLogDownloadInfo.TableName() + "(ident,url,path,filename,"
				+ "mime_type,size,put_time,status,create_time,business_id) VALUES(?,?,?,?,?,?,?,?,?,?)";
		getJdbcTemplate().update(sql, new Object[]{instance.getIdent(), instance.getUrl(), 
				instance.getPath(), instance.getFilename(), instance.getMimeType(), instance.getSize(),
				instance.getPutTime(), 0, instance.getCreateTime(),instance.getBusinessId()});
	}
	
	@Override
	public void batchSave(final List<? extends AppLogDownloadInfo> list) {
        //change by Jas@20140731 for avoid null exception
        if(list==null || list.size()<=0)return;
        //end change by Jas
		batch(new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AppLogDownloadInfo info = list.get(i);
				ps.setString(1, info.getIdent());
				ps.setString(2, info.getUrl());
				ps.setString(3, info.getPath());
				ps.setString(4, info.getFilename());
				ps.setString(5, info.getMimeType());
				ps.setLong(6, info.getSize());
				ps.setLong(7, info.getPutTime());
				ps.setInt(8, info.getStatus());
				ps.setTimestamp(9, info.getCreateTime());
                ps.setString(10, info.getBusinessId());
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
	}
	
	@Override
	public int updateStatus(long id, int status) {
		String sql = "UPDATE " + AppLogDownloadInfo.TableName() + " SET status=? WHERE id=?";
		return getJdbcTemplate().update(sql, status, id);
		
	}

	@Override
	public int updateStatus(AppLogDownloadInfo instance) {
		String sql = "UPDATE " + AppLogDownloadInfo.TableName() + " SET status=? WHERE id=?";
		return getJdbcTemplate().update(sql, instance.getStatus(), instance.getId());
		
	}
	
	@Override
	public boolean existIdent(String ident, String businessId) {
		String sql = "SELECT COUNT(*) FROM " + AppLogDownloadInfo.TableName() + " WHERE ident=? AND business_id=?";
		try{
			return getJdbcTemplate().queryForObject(sql, new Object[]{ident, businessId}, Integer.class)>0;
		} catch (Exception e){}
		return false;
	}

	private void batch(BatchPreparedStatementSetter setter) {
		if (setter == null) {
			return;
		}
		getJdbcTemplate().batchUpdate("INSERT INTO " + AppLogDownloadInfo.TableName() + "(ident,url,path,filename,"
				+ "mime_type,size,put_time,status,create_time,business_id) VALUES(?,?,?,?,?,?,?,?,?,?)",
			setter);
	}

}
