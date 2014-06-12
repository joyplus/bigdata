package tv.joyplus.backend.huan.dao.impl;

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
import tv.joyplus.backend.huan.dao.LogInfoDao;

public class LogInfoDaoDBImpl extends JdbcDaoSupport implements LogInfoDao {

	@Override
	public void batchLogInfo(final List<? extends LogInfo> list) {
		getJdbcTemplate().batchUpdate("INSERT IGNORE INTO md_log_info (`title`,`imgurl`,`adurl`,`sid`,"
				+ "`creative_id`,`create_time`,`zone_id`,`max_id`) VALUES (?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				LogInfo log = list.get(i);
				ps.setString(1, log.getTitle());
				ps.setString(2, log.getImgurl());
				ps.setString(3, log.getAdurl());
				ps.setString(4, log.getSid());
				ps.setLong(5, 0);
				ps.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
				ps.setLong(7, log.getZoneId());
				ps.setLong(8, 0);
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
	}

	@Override
	public List<LogInfo> findLogInfo() {
		String sql = "SELECT * FROM md_log_info WHERE creative_id <> 0 OR (title='' OR imgurl='')";
		return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<LogInfo>(LogInfo.class));
	}
	
	

	@Override
	public void updateMaxId(final LogInfo info) {
		String sql = "UPDATE md_log_info SET max_id=? WHERE title=? AND imgurl=?";
		getJdbcTemplate().update(sql, new PreparedStatementSetter(){

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, info.getMaxId());
				ps.setString(2, info.getTitle());
				ps.setString(3, info.getImgurl());
			}});
	}

	@Override
	public long getCampaignId(long creativeId) {
		if(creativeId==0) {
			return 0;
		}
		String sql = "SELECT campaign_id FROM md_ad_units WHERE adv_id=?";
		
		try {
			long l = getJdbcTemplate().queryForObject(sql, Long.class, creativeId);
			return l;
		}catch(Exception e) {
			return 0;
		}
	}

	@Override
	public long getPublicationId(long zoneId) {
		if(zoneId==0){
			return 0;
		}
		String sql = "SELECT publication_id FROM md_zones WHERE entry_id=?";
		try {
			long l = getJdbcTemplate().queryForObject(sql, Long.class, zoneId);
			return l;
		}catch(Exception e) {
			return 0;
		}
	}
	
}
