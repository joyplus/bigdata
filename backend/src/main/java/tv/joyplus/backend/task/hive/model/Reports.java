package tv.joyplus.backend.task.hive.model;

import java.io.Serializable;

public class Reports  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6315976045452636025L;
	private int id;
	private int campaign_id;
	private int ad_id;
	private int publication_id;
	private int zone_id;
	private int device_id;
	private String date;
	private String province_code;
	private String city_code;
	private int request;
	private int clicks;
	private int impression;
	private int uv;
	private int type;
	public int getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}
	public int getAd_id() {
		return ad_id;
	}
	public void setAd_id(int ad_id) {
		this.ad_id = ad_id;
	}
	public int getPublication_id() {
		return publication_id;
	}
	public void setPublication_id(int publication_id) {
		this.publication_id = publication_id;
	}
	public int getZone_id() {
		return zone_id;
	}
	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public int getRequest() {
		return request;
	}
	public void setRequest(int request) {
		this.request = request;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getImpression() {
		return impression;
	}
	public void setImpression(int impression) {
		this.impression = impression;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	@Override
	public String toString() {
		return "Reports [campaign_id=" + campaign_id + ", ad_id=" + ad_id
				+ ", publication_id=" + publication_id + ", zone_id=" + zone_id
				+ ", device_id=" + device_id + ", date=" + date
				+ ", province_code=" + province_code + ", city_code="
				+ city_code + ", request=" + request + ", clicks=" + clicks
				+ ", impression=" + impression + ", uv=" + uv + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
