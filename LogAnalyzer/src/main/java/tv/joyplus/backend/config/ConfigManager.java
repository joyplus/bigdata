package tv.joyplus.backend.config;

import java.util.Map;

/**
 * Created by zino on 14-7-17.
 */
public interface ConfigManager {
    public Map<String, String> getConfiguration(String key);
    public String getConfiguration(String key, String businessId);
}
