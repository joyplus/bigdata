package tv.joyplus.backend.huan.beans;

import java.sql.Timestamp;

public class AnalyzerFileInfo {
	public final static byte STATUS_UNPROCESSE = 0x00; 	//未处理
	public final static byte STATUS_PROCESSED = 0x01;	//已处理
	public final static byte STATUS_LOCK = 0x02;		//处理中
	public final static byte STATUS_EXIST = 0x04;		//已存在
	private long id;
	private String path;
	private String filename;
	private long offset;
	private int status;
	private Timestamp createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
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
		return "md_log_analyzer_info";
	}
}
