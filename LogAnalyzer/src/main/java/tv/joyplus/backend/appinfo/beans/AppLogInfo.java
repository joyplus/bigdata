package tv.joyplus.backend.appinfo.beans;

import java.sql.Timestamp;

public abstract class AppLogInfo {
	
	protected long id;
	protected String version;
	protected int status;
	protected Timestamp createTime;
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
	public static String TableName() {
		return "ap_app_log";
	}
}
