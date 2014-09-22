package tv.joyplus.backend.config;

import java.util.Map;

/**
 * Created by zino on 14-7-17.
 */
public interface ConfigManager {
    ///////////////////////////////////////////////////////////////////////////////
    /// This file was define to config AppLogAnalyzer Application config

    public Map<String, String> getConfiguration(String key);
    public String getConfiguration(String key, String businessId);
    //for list business ids for this AppLogAnalyzer need to analyzer
    public String[] getBusinessIds();
    public boolean containsBusinessId(String businessId);

    ///////////////////////////////////////////////////////////////////////////////
    // Interface for MutliThread Config
    public boolean getMultiThreadAnalyzeEn(String businessId);
    public String  getUseableTable(String businessId);
    public boolean releaseTable(String businessId,String tablename);
    public int     getTableSize();
    public String  getSampleThreadTable(String businessId);
}
