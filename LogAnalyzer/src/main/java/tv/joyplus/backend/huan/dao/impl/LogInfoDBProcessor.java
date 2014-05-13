package tv.joyplus.backend.huan.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.dao.LogInfoDao;

public class LogInfoDBProcessor extends JdbcDaoSupport implements LogInfoDao {

	@Override
	public void batchLogInfo(final List<? extends LogInfo> list) {
		getJdbcTemplate().batchUpdate("INSERT IGNORE INTO md_log_info (`title`,`imgurl`,`adurl`,`sid`,"
				+ "`creative_id`,`create_time`) VALUES (?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				LogInfo log = list.get(i);
				ps.setString(1, log.getTitle());
				ps.setString(2, log.getImgurl());
				ps.setString(3, log.getAdurl());
				ps.setString(4, log.getSid());
				ps.setLong(5, 0);
				ps.setTimestamp(6, new Timestamp(Calendar.getInstance().getTimeInMillis()));
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
}
