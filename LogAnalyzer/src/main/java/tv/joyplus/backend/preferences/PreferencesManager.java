package tv.joyplus.backend.preferences;

import java.util.Map;

/**
 * Created by Jas on 14-8-1.
 */
public interface PreferencesManager  {
    ///////////////////////////////////////////////////////////////////////////////
    /// This file was use to set preferences for this AppLogAnyalyzer OS running


    //add by Jas@20140801 for mutliThread
    //@Param: String bussinessId
    //@return Map<String,Boolean>:  String tablename, Boolean useable
    public Map<String ,Boolean> getMutliThreadConfig(String businessId);
    //@Param: String tablename , String businessId
    //@Return : boolean       for table useable
    public boolean getMutliThreadConfig(String tablename,String businessId);
    //@Param: String tablename ,String bussinessId , boolean useable
    public boolean setMutliThreadConfig(String tablename,String businessId,boolean useable);
}
