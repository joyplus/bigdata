package tv.joyplus.backend.huan.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.huan.beans.LogData;
import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.dao.LogDataDao;

public class LogDataDaoDBImpl extends JdbcDaoSupport implements LogDataDao {

	@Override
	public void batchLogData(final List<? extends LogData> list) {
		getJdbcTemplate().batchUpdate("INSERT INTO md_log_data (`ad_date`,`equipment_key`,`device_name`,"
				+ "`version`,`ip`,`imgurl`,`adurl`,`sid`,`title`,`zone_id`,`create_time`,`status`) VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				LogData log = list.get(i);
				ps.setObject(1, log.getAdDate());
				ps.setObject(2, log.getEquipmentKey());
				ps.setObject(3, log.getDeviceName());
				ps.setObject(4, log.getVersion());
				ps.setObject(5, log.getIp());
				ps.setString(6, log.getImgurl());
				ps.setString(7, log.getAdurl());
				ps.setString(8, log.getSid());
				ps.setString(9, log.getTitle());
				ps.setLong(10, log.getZoneId());
				ps.setTimestamp(11, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setInt(12, 0);
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public List<LogData> find(final LogInfo info) {
		String sql = null;
		if(info.getMaxId()==0) {
			sql = "SELECT * FROM md_log_data WHERE title=? AND imgurl=? ORDER BY id DESC";
		}else{
			sql = "SELECT * FROM md_log_data WHERE title=? AND imgurl=? AND id>" + info.getMaxId() + " ORDER BY id DESC";
		}
		return getJdbcTemplate().query(sql, new PreparedStatementSetter(){

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, info.getTitle());
					ps.setString(2, info.getImgurl());
				}
			}, new BeanPropertyRowMapper<LogData>(LogData.class));
	}

	@Override
	public Connection GetConnection() {
		return getConnection();
	}
	
	
	
}
