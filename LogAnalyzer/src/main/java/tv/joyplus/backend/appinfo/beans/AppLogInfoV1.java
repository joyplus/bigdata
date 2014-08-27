package tv.joyplus.backend.appinfo.beans;

import tv.joyplus.backend.exception.TaskException;
import tv.joyplus.backend.utils.FormatTool;

import java.sql.Timestamp;
import java.util.Properties;


public class AppLogInfoV1 extends AppDeviceInfoV1 {

    public AppLogInfoV1() throws TaskException {
        this(null);
    }

    public AppLogInfoV1(Properties prop, String line, String businessId,String tablename) throws TaskException {
        this(prop, line);
        this.businessId = businessId;
        this.tableName  = tablename;
        check();
    }
    public AppLogInfoV1(Properties prop, String line, String businessId) throws TaskException {
        this(prop, line,businessId,null);
    }

    public AppLogInfoV1(Properties prop) throws TaskException {
        super(prop);
    }

    public AppLogInfoV1(Properties prop, String line) throws TaskException {
        super(prop);
        parser(line);
    }
    private void check() throws TaskException {
        if(FormatTool.isEmpty(tableName))
            throw new TaskException("table unuse "+tableName);
    }
    public void parser(String line) throws TaskException {
        if (line == null || line.trim().length() <= 0) {
            throw new TaskException("info unuse");
        }
        String[] strs = line.split(",");
        if (strs == null || strs.length != 12) {
            throw new TaskException("info unuse length is "+(strs==null?"null":strs.length));
        }
        packageName = removeQuotation(strs[0]);
        appName = removeQuotation(strs[1]).trim();
        versionCode = removeQuotation(strs[2]);
        versionName = removeQuotation(strs[3]);

        //sdk version大于8 才会取得到值
        firstInstallTime = removeQuotation(strs[4]).length() <= 0 ? null : new Timestamp(Long.valueOf(removeQuotation(strs[4])));
        lastUpdateTime = removeQuotation(strs[5]).length() <= 0 ? null : new Timestamp(Long.valueOf(removeQuotation(strs[5])));
        front = removeQuotation(strs[6]);
        timeStart = new Timestamp(Long.valueOf(removeQuotation(strs[7])));
        timeEnd = new Timestamp(Long.valueOf(removeQuotation(strs[8])));
        baseCompent = removeQuotation(strs[9]);
        topCompent = removeQuotation(strs[10]);
        firstStart = Integer.valueOf(removeQuotation(strs[11]));
        if (packageName == null || packageName.length() <= 0) {
            packageName = topCompent.split("/")[0];
        }

        if (packageName == null || packageName.length() <= 0) {
            throw new TaskException("unknow package name");
        }
    }

    /**
     * 去除双引号
     * @param str
     * @return
     * @throws TaskException
     */
    private String removeQuotation(String str) throws TaskException {
        if (str == null || str.trim().length() <= 2) {
            return "";
        }
        if ('"' == str.charAt(0) && '"' == str.charAt(str.length() - 1)) {
            return str.substring(1, str.length() - 1);
        }
        throw new TaskException("info unuse removeQuotation ");
    }

    private String packageName;
    private String appName;
    private String versionCode;
    private String versionName;
    private Timestamp firstInstallTime;
    private Timestamp lastUpdateTime;
    private String front;
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private String baseCompent;
    private String topCompent;
    private int firstStart;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Timestamp getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(Timestamp firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getBaseCompent() {
        return baseCompent;
    }

    public void setBaseCompent(String baseCompent) {
        this.baseCompent = baseCompent;
    }

    public String getTopCompent() {
        return topCompent;
    }

    public void setTopCompent(String topCompent) {
        this.topCompent = topCompent;
    }

    public int getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(int firstStart) {
        this.firstStart = firstStart;
    }

    public static String TableName() {
        return "ap_app_log";
    }

}
