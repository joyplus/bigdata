package tv.joyplus.backend.appinfo.dao;

import tv.joyplus.backend.appinfo.beans.AppLogInfo;

import java.util.List;

public interface AppLogDao {
    public void save(AppLogInfo instance);

    public void batchSave(List<? extends AppLogInfo> list);
}
