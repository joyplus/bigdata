package tv.joyplus.backend.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tv.joyplus.backend.appinfo.Config;
import tv.joyplus.backend.appinfo.beans.AppLogInfo;
import tv.joyplus.backend.utils.FormatTool;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-8-1.
 */

public class MutliThreadPreferences {

    public static final Log log = LogFactory.getLog(MutliThreadPreferences.class);
    private static final String KEY_MutliThread = "MutliThreadEn";
    public MutliThreadPreferences(ConfigManagerImpl configManager){
        Prefences = new HashMap<String, Map<String, Boolean>>();
        if(configManager!=null && configManager.getBusinessIds()!=null){
            initMutliThreadData(configManager.getBusinessIds(), configManager.getConfiguration(Config.LocationKey.APP_LOG_MUTLITHREAD));
            initTableData(configManager.getBusinessIds(), configManager.getConfiguration(Config.LocationKey.APP_LOG_TAB));
        }else{
            log.debug("MutliThreadPreferences ConfigManager is null");
        }
    }
    private void initMutliThreadData(String[] businessIds ,Map<String, String> map){
        if((businessIds==null||businessIds.length<0)||(map==null||map.size()<=0))return;
        for(String businessId:businessIds){
            initMutliThreadData(businessId,map);
        }
    }
    private void initMutliThreadData(String businessId,Map<String, String> map){
        if(FormatTool.isEmpty(businessId)||(!map.containsKey(businessId)))return;
        String en = map.get(businessId);
        addData(businessId,KEY_MutliThread,(!FormatTool.isEmpty(en)&&(en.toLowerCase().equals("true"))));
    }

    private void initTableData(String[] businessIds ,Map<String, String> map){
        if((businessIds==null||businessIds.length<0)||(map==null||map.size()<=0))return;
        for(String businessId:businessIds){
            initTableData(businessId,map);
        }
    }
    private void initTableData(String businessId,Map<String, String> map){
        if(FormatTool.isEmpty(businessId)||(!map.containsKey(businessId)))return;
        String table = map.get(businessId);
        if(!FormatTool.isEmpty(table)){
            for(String tableNumber : table.split(",")){
                addData(businessId,("0".equals(tableNumber))? AppLogInfo.TableName():(AppLogInfo.TableName()+tableNumber.trim()),false);
            }
        }
    }
//    private void initData(final Map<String, String> map){
//        if(map==null||map.size()<=0)return;
//        for(final String businessId : map.keySet()){
//             String table = map.get(businessId);
//             log.debug("table*********************************** "+table);
//             if(!FormatTool.isEmpty(table)){
//                 for(String tableNumber : table.split(",")){
//                     addData(businessId,("0".equals(tableNumber))? AppLogInfo.TableName():(AppLogInfo.TableName()+tableNumber.trim()));
//                 }
//             }
//        }
//        log.debug(toString());
//    }
    private void addData(String businessId,final String key, final boolean value){
        if(FormatTool.isEmpty(businessId)|| FormatTool.isEmpty(key))return;
        if(Prefences.containsKey(businessId)){
            Prefences.get(businessId).put(key,value);
        }else{
            Prefences.put(businessId, new HashMap<String, Boolean>(){{
                put(key , value);
            }});
        }
    }

    private Map<String, Map<String,Boolean>> Prefences;// <businessId,<tablename,usable>>
    private boolean getMutliThreadConfig(String tablename, String businessId) {
           return Prefences.containsKey(businessId) ? Prefences.get(businessId).get(tablename) : true;
    }

    private Map<String, Boolean> getMutliThreadConfig(String businessId) {
           return Prefences.get(businessId);
    }

    private boolean setMutliThreadConfig(String tablename, String businessId, boolean useable) {
           return Prefences.containsKey(businessId)?
                   (Prefences.get(businessId).containsKey(tablename)?Prefences.get(businessId).put(tablename,useable):false)
                   :false;
    }

    public String  getUseableTable(String businessId){
        synchronized (Prefences){
            Map<String, Boolean> map = getMutliThreadConfig(businessId);
            if(map!=null&&map.size()>0){
                for(Object key : map.keySet()){
                    if((!map.get(key))){
                          setMutliThreadConfig((String)key,businessId,true);
                          return (String)key;
                    }
                }
            }
        }
        return null;
    }
    public boolean releaseTable(String businessId,String tablename){
        synchronized (Prefences){
            return setMutliThreadConfig(tablename,businessId,false);
        }
    }
    public int     getTableSize(){
        return Prefences.size();
    }
    public boolean getMultiThreadAnalyzeEn(String businessId){
        return getMutliThreadConfig(KEY_MutliThread,businessId);
    }
    @Override
    public String toString() {
        String result = "{";
        for(Object key : Prefences.keySet()){
            result+=(key+"{"+_toString((Map)Prefences.get(key))+"},");
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
