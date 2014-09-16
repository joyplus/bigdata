package tv.joyplus.backend.task.hive.model;

import java.io.Serializable;

public class PublicationReports  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5008134248272777832L;
	
	private int id;
	private Integer campaign_id;
	private String campaign_name;
	private int publication_id;
	private String publication_name;
	private int zone_id;
	private String zone_name;
	private String date;
	private int impression;
	private int uv;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCampaign_name() {
		return campaign_name;
	}
	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}
	public int getPublication_id() {
		return publication_id;
	}
	public void setPublication_id(int publication_id) {
		this.publication_id = publication_id;
	}
	public String getPublication_name() {
		return publication_name;
	}
	public void setPublication_name(String publication_name) {
		this.publication_name = publication_name;
	}
	public int getZone_id() {
		return zone_id;
	}
	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public Integer getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}
}
