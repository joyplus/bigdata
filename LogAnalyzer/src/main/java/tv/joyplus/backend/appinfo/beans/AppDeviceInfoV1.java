package tv.joyplus.backend.appinfo.beans;

import tv.joyplus.backend.exception.TaskException;

import java.util.Properties;

public abstract class AppDeviceInfoV1 extends AppLogInfo {
    public AppDeviceInfoV1() throws TaskException {
        this(null);
    }

    public AppDeviceInfoV1(Properties prop) throws TaskException {
        if (prop != null) {
            transfor(prop);
        }
    }

    protected String devicesname;
    protected String sdkVersion;
    protected String mac;
    protected String displayW;
    protected String displayH;
    protected String ip;

    public String getDevicesname() {
        return devicesname;
    }

    public void setDevicesname(String devicesname) {
        this.devicesname = devicesname;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDisplayW() {
        return displayW;
    }

    public void setDisplayW(String displayW) {
        this.displayW = displayW;
    }

    public String getDisplayH() {
        return displayH;
    }

    public void setDisplayH(String displayH) {
        this.displayH = displayH;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void transfor(Properties prop) throws TaskException {
        if (prop == null || !"1.0".equals(prop.getProperty("version"))) {
            throw new TaskException("Error Version need 1.0 but " + (prop == null ? " prop is null" : (prop.getProperty("version"))));
        }
        version = prop.getProperty("version", "");
        devicesname = prop.getProperty("devicesname", "");
        sdkVersion = prop.getProperty("sdk");
        mac = prop.getProperty("mac", "");
        displayW = prop.getProperty("display_w", "");
        displayH = prop.getProperty("display_h", "");
        ip = prop.getProperty("ip", "");
    }
}
