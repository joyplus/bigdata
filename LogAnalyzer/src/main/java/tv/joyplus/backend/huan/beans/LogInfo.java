package tv.joyplus.backend.huan.beans;

public class LogInfo {
	private String date;	//时间
	private String dnum;	//参数 i
	private String devmodel;//参数 dm
	private String version;	//版本
	private String ip;		//客户端ip
	private String imgurl;	//素材地址
	private String adurl;	//监测url
	private String sid;		//
	private String title;	//
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDnum() {
		return dnum;
	}
	public void setDnum(String dnum) {
		this.dnum = dnum;
	}
	public String getDevmodel() {
		return devmodel;
	}
	public void setDevmodel(String devmodel) {
		this.devmodel = devmodel;
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
	@Override
	public String toString() {
		return "\""+date+"\",\""+dnum+"\",\""+devmodel+"\",\""+version
				+"\",\""+ip+"\",\""+imgurl+"\",\""+adurl+"\",\""
				+sid+"\",\""+title+"\"";
	}
	
}
