package tv.joyplus.backend.qiniu;

public class QiniuItem {
	private String key;
	private String hash;
	private long fsize;
	private long putTime;
	private String mimeType;
	private String endUser;
	
	public QiniuItem() {
		
	}
	public QiniuItem(String key, String hash, long fsize, long putTime, String mimeType, String endUser) {
		this.key = key;
		this.hash = hash;
		this.fsize = fsize;
		this.putTime = putTime;
		this.mimeType = mimeType;
		this.endUser = endUser;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public long getFsize() {
		return fsize;
	}
	public void setFsize(long fsize) {
		this.fsize = fsize;
	}
	public long getPutTime() {
		return putTime;
	}
	public void setPutTime(long putTime) {
		this.putTime = putTime;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getEndUser() {
		return endUser;
	}
	public void setEndUser(String endUser) {
		this.endUser = endUser;
	}
	
}
