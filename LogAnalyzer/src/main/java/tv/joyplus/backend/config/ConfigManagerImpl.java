package tv.joyplus.backend.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.*;

/**
 * Created by zino on 14-7-17.
 */
public class ConfigManagerImpl extends JdbcDaoSupport implements ConfigManager {
    private final static Log log = LogFactory.getLog(ConfigManagerImpl.class);
    private Map<String, Map<String,String>> Configs;
    private String[] businessIds;
    @Override
    public Map<String, String> getConfiguration(String key) {
        return Configs.get(key);
    }

    @Override
    public String getConfiguration(String key, String businessId) {
        return Configs.containsKey(key)?Configs.get(key).get(businessId):null;
    }

    @Override
    protected void initTemplateConfig() {
        Configs = new HashMap<>();
        List<ConfigProperty> configurations = loadConfigurations();
        for(final ConfigProperty c : configurations) {
            log.debug(c.toString());
            if(Configs.containsKey(c.getConfigKey())){
                Configs.get(c.getConfigKey()).put(c.getBusinessId(), c.getConfigValue());
            }else{
                Configs.put(c.getConfigKey(), new HashMap<String, String>(){{
                    put(c.getBusinessId(), c.getConfigValue());
                }});
            }
        }
        log.debug(toString());
    }

    private List<ConfigProperty> loadConfigurations() {
        int count = businessIds.length;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(ConfigProperty.TableName());
        sql.append(" WHERE business_id IN (");
        for (int i = 0; i < count; i++) {
            sql.append("'" + businessIds[i] + "'");
            if (i < count - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<>(ConfigProperty.class));
    }


    public void setBusinessIds(String businessIds) {
        log.debug("setBusinessIds");
        this.businessIds = businessIds.split(",");
    }

    @Override
    public String toString() {
        String result = "{";
        for(Object key : Configs.keySet()){
           result+=(key+"{"+_toString((Map)Configs.get(key))+"}");
        }
        return result+"}";
    }
    private String _toString(Map map){
        String result = "";
        for(Object key : map.keySet()){
            result+=(key+":"+map.get(key));
        }
        return result;
    }

}
