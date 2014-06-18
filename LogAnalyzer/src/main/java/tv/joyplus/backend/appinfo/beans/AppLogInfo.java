package tv.joyplus.backend.appinfo.beans;

import java.sql.Timestamp;

public class AppLogInfo {
	private long id;
	private String version;
	private String devicesname;
	private String mac;
	private String displayW;
	private String displayH;
	private String ip;
	private String appName;
	private String front;
	private long timeStart;
	private long timeEnd;
	private String baseCompent;
	private String topCompent;
	private int status;
	private Timestamp createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDevicesname() {
		return devicesname;
	}
	public void setDevicesname(String devicesname) {
		this.devicesname = devicesname;
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
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}
	public long getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	public long getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(long timeEnd) {
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public static String tableName() {
		return "md_app_log";
	}
}
