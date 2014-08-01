package tv.joyplus.backend.preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-8-1.
 */
public class PreferencesManagerImpl extends JdbcDaoSupport implements PreferencesManager {
    private final static Log log = LogFactory.getLog(PreferencesManager.class);


    @Override
    public boolean getMutliThreadConfig(String tablename, String businessId) {
        return false;
    }

    @Override
    public Map<String, Boolean> getMutliThreadConfig(String businessId) {
        return null;
    }

    @Override
    public boolean setMutliThreadConfig(String tablename, String businessId, boolean useable) {
        return false;
    }

}
