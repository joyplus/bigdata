package tv.joyplus.backend.appinfo.dao.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.appinfo.beans.AppLogProcessInfo;
import tv.joyplus.backend.appinfo.dao.AppLogProcessDao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zino on 14-7-7.
 */
public class AppLogProcessDaoImpl extends JdbcDaoSupport implements AppLogProcessDao {
    @Override
    public AppLogProcessInfo lastTime(final String businessId) {
        String sql = "SELECT id,last_execute_time,business_id FROM " + AppLogProcessInfo.TableName() + " WHERE business_id=? ORDER BY id DESC";
        List<AppLogProcessInfo> list = getJdbcTemplate().query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, businessId);
            }
        }, new BeanPropertyRowMapper<AppLogProcessInfo>(AppLogProcessInfo.class));
        if(list==null || list.size()<=0) {
            return null;
        }
        return list.iterator().next();
    }

    @Override
    public void update(long id, Timestamp time) {
        String sql = "UPDATE " + AppLogProcessInfo.TableName() + " SET last_execute_time=? WHERE id=?";
        getJdbcTemplate().update(sql, new Object[]{time, id});
    }

    @Override
    public void update(AppLogProcessInfo instance) {
        String sql = "UPDATE " + AppLogProcessInfo.TableName() + " SET last_execute_time=? WHERE id=?";
        getJdbcTemplate().update(sql, new Object[]{instance.getLastExecuteTime(), instance.getId()});
    }

    @Override
    public void save(AppLogProcessInfo instance) {
        String sql = "INSERT INTO " + AppLogProcessInfo.TableName() + "(last_execute_time,business_id,create_time) VALUES(?,?,?)";
        getJdbcTemplate().update(sql, new Object[]{instance.getLastExecuteTime(), instance.getBusinessId(), new Timestamp(System.currentTimeMillis())});
    }
}
