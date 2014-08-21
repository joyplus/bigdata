package tv.joyplus.backend.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.utils.FormatTool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String[] getBusinessIds(){
        return businessIds;
    }
    ///////////////////////////////////////////////////////
    // for MutliThread Config
    private MutliThreadPreferences mutliThreadPreferences;
    public String  getUseableTable(String businessId){
            return mutliThreadPreferences.getUseableTable(businessId);
    }
    public boolean releaseTable(String businessId,String tablename){
            return mutliThreadPreferences.releaseTable(businessId,tablename);
    }
    public int     getTableSize(){
            return mutliThreadPreferences.getTableSize();
    }
    public boolean getMultiThreadAnalyzeEn(String businessId){return mutliThreadPreferences.getMultiThreadAnalyzeEn(businessId);}
    public String  getSampleThreadTable(String businessId){
        if(containsBusinessId(businessId))
             return AppLogInfo.TableName();
        return null;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void initTemplateConfig() {
        Configs = new HashMap<String, Map<String,String>>();
        List<ConfigProperty> configurations = loadConfigurations();
        for(final ConfigProperty c : configurations) {
            //add by Jas for make sure only load analyze businessId config in this application.
            if(!containsBusinessId(c.getBusinessId()))continue;
            log.debug(c.toString());
            if(Configs.containsKey(c.getConfigKey())){
                Configs.get(c.getConfigKey()).put(c.getBusinessId(), c.getConfigValue());
            }else{
                Configs.put(c.getConfigKey(), new HashMap<String, String>(){{
                    put(c.getBusinessId(), c.getConfigValue());
                }});
            }
        }
        mutliThreadPreferences = new MutliThreadPreferences(ConfigManagerImpl.this);
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
        return getJdbcTemplate().query(sql.toString(), new BeanPropertyRowMapper<ConfigProperty>(ConfigProperty.class));
    }


    public void setBusinessIds(String businessIds) {
        log.debug("setBusinessIds");
        this.businessIds = businessIds.split(",");
    }
    public boolean containsBusinessId(String businessId){
        if(FormatTool.isEmpty(businessId)||businessIds==null)return false;
        for(String id:businessIds){
            if(id.equals(businessId))return true;
        }
        return false;
    }
    @Override
    public String toString() {
        String result = "ConfigManager{";
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
