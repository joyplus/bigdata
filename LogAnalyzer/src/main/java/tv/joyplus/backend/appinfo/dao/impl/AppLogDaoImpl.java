package tv.joyplus.backend.appinfo.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.appinfo.beans.AppLogInfoV1;
import tv.joyplus.backend.appinfo.dao.AppLogDao;

public class AppLogDaoImpl extends JdbcDaoSupport implements AppLogDao {

	@Override
	public void save(AppLogInfo instance) {

	}

	@Override
	public void batchSave(final List<? extends AppLogInfo> list) {
		batch(new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				prepareAppLogInfo(ps, list.get(i));
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	private void prepareAppLogInfo(PreparedStatement ps, AppLogInfo info)
			throws SQLException {
		if (info == null)
			return;
		if ("1.0".equals(info.getVersion())) {
			prepareAppLogInfo(ps, (AppLogInfoV1) info);
		}
	}

	private void prepareAppLogInfo(PreparedStatement ps, AppLogInfoV1 info)
			throws SQLException {
		// version,device_name,sdk_version,mac,display_w,display_h,ip,package_name,app_name,version_code,version_name,first_install_time,last_update_time,front,time_start,time_end,base_compent,top_compent,status,create_time
		Object[] mObject = new Object[] { 
				info.getVersion(),
				info.getDevicesname(), 
				info.getSdkVersion(), 
				info.getMac(), 
				info.getDisplayW(),
				info.getDisplayH(), 
				info.getIp(),
				info.getPackageName(),
				info.getAppName(),
				info.getVersionCode(),
				info.getVersionName(),
				info.getFirstInstallTime(),
				info.getLastUpdateTime(),
				info.getFront(), 
				info.getTimeStart(), 
				info.getTimeEnd(),
				info.getBaseCompent(), 
				info.getTopCompent(), 
				info.getStatus(),
				new Timestamp(Calendar.getInstance().getTimeInMillis()) };
		prepareAppLogInfo(ps, mObject);

	}

	private void prepareAppLogInfo(PreparedStatement ps, Object[] infos)
			throws SQLException {
		for (int i = 0; i < infos.length; i++) {
			ps.setObject(i + 1, infos[i]);
		}
	}

	private void batch(BatchPreparedStatementSetter setter) {
		if (setter == null) {
			return;
		}
		getJdbcTemplate().batchUpdate("INSERT INTO "
			+ AppLogInfo.TableName()
			+ " (version,device_name,sdk_version,mac,display_w,display_h,ip,"
			+ "package_name,app_name,version_code,version_name,first_install_time,"
			+ "last_update_time,front,time_start,time_end,base_compent,top_compent,status,create_time) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
			setter);
	}

}
