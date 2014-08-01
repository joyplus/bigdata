package tv.joyplus.backend.preferences;

import java.util.Map;

/**
 * Created by Administrator on 14-8-1.
 */

public class MutliThreadPreferences {

    private Map<String, Map<String,Boolean>> Prefences;
    public boolean getMutliThreadConfig(String tablename, String businessId) {
        return false;
    }


    public Map<String, Boolean> getMutliThreadConfig(String businessId) {
        return null;
    }


    public boolean setMutliThreadConfig(String tablename, String businessId, boolean useable) {
        return false;
    }



}
