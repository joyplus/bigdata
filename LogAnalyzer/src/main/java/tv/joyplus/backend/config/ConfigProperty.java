package tv.joyplus.backend.config;

/**
 * Created by zino on 14-7-17.
 */
public class ConfigProperty {
    private String configKey;
    private String configValue;
    private String businessId;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public static String TableName() {
        return "md_process_config";
    }

    @Override
    public String toString() {
        return "ConfigProperty{" +
                "configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", businessId='" + businessId + '\'' +
                '}';
    }
}
