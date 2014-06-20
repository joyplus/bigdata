package tv.joyplus.backend.appinfo.beans;

import java.util.Properties;

import tv.joyplus.backend.exception.TaskException;

public class AppLogInfoV1 extends AppDeviceInfoV1{
	
	public AppLogInfoV1() throws TaskException {
		this(null);
	}
	public AppLogInfoV1(Properties prop) throws TaskException {
		super(prop);
	}
	
	public AppLogInfoV1(Properties prop, String line) throws TaskException {
		super(prop);
		parser(line);
	}
	
	public void parser(String line) throws TaskException {
		if(line==null || line.trim().length()<=0) {
			throw new TaskException("info unuse");
		}
		String[] strs = line.split(",");
		if(strs==null || strs.length!=6) {
			throw new TaskException("info unuse");
		}
		appName = strs[0];
		front = strs[1];
		timeStart = Long.valueOf(strs[2])/1000;
		timeEnd = Long.valueOf(strs[3])/1000;
		baseCompent = strs[4];
		topCompent = strs[5];
	}
	private String appName;
	private String front;
	private long timeStart;
	private long timeEnd;
	private String baseCompent;
	private String topCompent;
	
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
	public static String TableName() {
		return "ap_app_log";
	}
}
