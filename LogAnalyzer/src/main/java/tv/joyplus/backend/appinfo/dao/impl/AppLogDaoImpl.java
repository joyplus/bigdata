package tv.joyplus.backend.appinfo.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.appinfo.beans.AppLogInfoV1;
import tv.joyplus.backend.appinfo.dao.AppLogDao;
import tv.joyplus.backend.utils.FormatTool;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class AppLogDaoImpl extends JdbcDaoSupport implements AppLogDao {

    @Override
    public void save(AppLogInfo instance) {

    }

    @Override
    public void batchSave(final List<? extends AppLogInfo> list) {
        //add by Jas@20140731 for avoid null exception
        if(list==null||list.size()<=0)return;
        //end add by Jas
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
        },list.get(0).getTableName());//use the first AppLogInfo tableName for save.
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
        // version,device_name,sdk_version,mac,display_w,display_h,ip,package_name,app_name,version_code,version_name,first_install_time,last_update_time,front,time_start,time_end,base_compent,top_compent,status,create_time,business_id
        Object[] mObject = new Object[]{
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
                info.getFirstStart(),
                info.getStatus(),
                new Timestamp(Calendar.getInstance().getTimeInMillis()),
                info.getBusinessId()};
        prepareAppLogInfo(ps, mObject);

    }

    private void prepareAppLogInfo(PreparedStatement ps, Object[] infos)
            throws SQLException {
        for (int i = 0; i < infos.length; i++) {
            ps.setObject(i + 1, infos[i]);
        }
    }

    private void batch(BatchPreparedStatementSetter setter,String tableName) {

        if (setter == null || FormatTool.isEmpty(tableName)) {
            return;
        }
        getJdbcTemplate().batchUpdate("INSERT INTO "
                        + tableName
                        + " (version,device_name,sdk_version,mac,display_w,display_h,ip,"
                        + "package_name,app_name,version_code,version_name,first_install_time,"
                        + "last_update_time,front,time_start,time_end,base_compent,top_compent,first_start,status,create_time,business_id) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                setter);
    }

}
