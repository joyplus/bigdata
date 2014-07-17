package tv.joyplus.backend.huan.dao;

import java.util.List;
import java.util.Map;

public interface LogProcessDao {
    public void sendToRequestLog(List<Map> list);

    public void sendToRequestLog(Map map);

    public void sendToReporting(List<Map> list);

    public void sendToReporting(Map map);
}
