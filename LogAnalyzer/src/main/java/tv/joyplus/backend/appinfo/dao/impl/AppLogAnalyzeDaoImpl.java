package tv.joyplus.backend.appinfo.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;

import java.util.List;

public class AppLogAnalyzeDaoImpl extends JdbcDaoSupport implements
		AppLogAnalyzeDao {

	@Override
	public void save(AppLogAnalyzeInfo instance) {
		String sql = "INSERT INTO " + AppLogAnalyzeInfo.TableName() + "(path,status,create_time,business_id) VALUES(?,?,?,?)";
		getJdbcTemplate().update(sql, new Object[]{instance.getPath(), instance.getStatus(), instance.getCreate_time(),instance.getBusinessId()});
	}

	@Override
	public List<AppLogAnalyzeInfo> list() {
		String sql = "SELECT * FROM " + AppLogAnalyzeInfo.TableName();
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<AppLogAnalyzeInfo>(AppLogAnalyzeInfo.class));
	}

	@Override
	public List<AppLogAnalyzeInfo> listUnAnalyzed() {
		String sql = "SELECT * FROM " + AppLogAnalyzeInfo.TableName() + " WHERE status=0";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<AppLogAnalyzeInfo>(AppLogAnalyzeInfo.class));
	}

	@Override
	public void updateStatus(long id, int status) {
		String sql = "UPDATE " + AppLogAnalyzeInfo.TableName() + " SET status=? WHERE id=?";
		getJdbcTemplate().update(sql, new Object[]{status, id});
	}

	@Override
	public void updateStatus(AppLogAnalyzeInfo instance) {
		String sql = "UPDATE " + AppLogAnalyzeInfo.TableName() + " SET status=? WHERE id=?";
		getJdbcTemplate().update(sql, new Object[]{instance.getStatus(), instance.getId()});
	}

}
