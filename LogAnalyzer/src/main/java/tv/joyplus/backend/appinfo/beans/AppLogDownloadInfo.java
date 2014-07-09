package tv.joyplus.backend.appinfo.beans;

import java.sql.Timestamp;

public class AppLogDownloadInfo {
	public static final int STATUS_UNDOWNLOAD = 0; //未下载
	public static final int STATUS_DOWNLOADING = 1;//下载中
	public static final int STATUS_DOWNLOADED = 2; //已下载
	private long id;
	private String ident;
	private String url;
	private String path;
	private String filename;
	private String mimeType;
	private long size;
	private long putTime;
	private int status;
	private Timestamp createTime;
    private String businessId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getPutTime() {
		return putTime;
	}
	public void setPutTime(long putTime) {
		this.putTime = putTime;
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

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public static String TableName() {
		return "ap_app_log_download_info";
	}
}
