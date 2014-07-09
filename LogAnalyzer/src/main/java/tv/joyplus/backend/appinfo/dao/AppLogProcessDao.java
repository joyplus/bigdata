package tv.joyplus.backend.appinfo.dao;

import tv.joyplus.backend.appinfo.beans.AppLogProcessInfo;

import java.sql.Timestamp;

/**
 * Created by zino on 14-7-7.
 */
public interface AppLogProcessDao {
    public AppLogProcessInfo lastTime(final String businessId);
    public void update(long id, Timestamp time);
    public void update(AppLogProcessInfo instance);
    public void save(AppLogProcessInfo instance);
}
