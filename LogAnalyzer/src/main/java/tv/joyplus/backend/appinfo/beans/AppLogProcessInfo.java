package tv.joyplus.backend.appinfo.beans;


import java.sql.Timestamp;

/**
 * Created by zino on 14-7-7.
 */
public class AppLogProcessInfo {
    private long id;
    private Timestamp lastExecuteTime;
    private String businessId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Timestamp lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public static String TableName() {
        return "ap_process_info";
    }
}
