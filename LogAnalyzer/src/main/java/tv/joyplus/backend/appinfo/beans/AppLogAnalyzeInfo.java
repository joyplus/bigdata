package tv.joyplus.backend.appinfo.beans;

import java.sql.Timestamp;

public class AppLogAnalyzeInfo {
	public final static byte STATUS_UNPROCESSE = 0x00; 	//未处理
	public final static byte STATUS_PROCESSED = 0x01;	//已处理
	public final static byte STATUS_LOCK = 0x02;		//处理中
	public final static byte STATUS_EXIST = 0x04;		//已存在
	private long id;
	private String path;
	private String filename;
	private int status;
	private Timestamp create_time;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public static String tableName() {
		return "md_app_log_analyze_info";
	}
}
