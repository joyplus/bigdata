package tv.joyplus.backend.huan.dao;

import java.util.List;
import java.util.Map;

public interface LogProcessDao {
	public void batch(List<Map> list);
	public void process(Map map);
}
