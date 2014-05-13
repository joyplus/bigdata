package tv.joyplus.backend.huan.beans;


public class LogInfo {
	public final static byte STATUS_UNPROCESSE = 0x00;
	public final static byte STATUS_PROCESSED = 0x01;
	private String adDate;	//时间
	private String equitpmentKey;	//参数 i
	private String deviceName;//参数 dm
	private String version;	//版本
	private String ip;		//客户端ip
	private String imgurl;	//素材地址
	private String adurl;	//监测url
	private String sid;		//
	private String title;	//
	private long zoneId;
	private byte status;

	public String getAdDate() {
		return adDate;
	}
	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}
	public String getEquitpmentKey() {
		return equitpmentKey;
	}
	public void setEquitpmentKey(String equitpmentKey) {
		this.equitpmentKey = equitpmentKey;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getAdurl() {
		return adurl;
	}
	public void setAdurl(String adurl) {
		this.adurl = adurl;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "\""+adDate+"\",\""+equitpmentKey+"\",\""+deviceName+"\",\""+version
				+"\",\""+ip+"\",\""+imgurl+"\",\""+adurl+"\",\""
				+sid+"\",\""+title+"\"";
	}

}
