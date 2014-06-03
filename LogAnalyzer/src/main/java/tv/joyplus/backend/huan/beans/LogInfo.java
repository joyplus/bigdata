package tv.joyplus.backend.huan.beans;


public class LogInfo {
	
	private String imgurl;	//素材地址
	private String adurl;
	private String sid;		//
	private String title;	//
	private long zoneId;
	private long maxId;
	private long creativeId;
	private byte status;
	
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
	public long getMaxId() {
		return maxId;
	}
	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}
	public long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
}
