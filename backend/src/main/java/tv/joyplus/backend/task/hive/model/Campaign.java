package tv.joyplus.backend.task.hive.model;

import java.io.Serializable;

public class Campaign implements Serializable {

	private static final long serialVersionUID = -6523204144285350002L;
	
	private int id;
	private Integer campaign_owner;
	private String status;
	private String type;
	private String name;
	private String desc;
	private String date_start;
	private String date_end;
	private String creationdate;
	private String networkid;
	private Integer priority;
	private String country_target;
	private String publication_target;
	private String channel_target;
	private String device_target;
	private String device_type_target;
	private String video_target;
	private String pattern_target;
	private String quality_target;
	private String brand_target;
	private String creative_show_rule;
	private Integer belong_to_advertiser;
	private Integer campaign_display_way;
	private Integer total_amount;
	private Integer campaign_class;
	private Integer time_target;
	private String hash;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getCampaign_owner() {
		return campaign_owner;
	}
	public void setCampaign_owner(Integer campaign_owner) {
		this.campaign_owner = campaign_owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDate_start() {
		return date_start;
	}
	public void setDate_start(String date_start) {
		this.date_start = date_start;
	}
	public String getDate_end() {
		return date_end;
	}
	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}
	public String getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}
	public String getNetworkid() {
		return networkid;
	}
	public void setNetworkid(String networkid) {
		this.networkid = networkid;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getCountry_target() {
		return country_target;
	}
	public void setCountry_target(String country_target) {
		this.country_target = country_target;
	}
	public String getPublication_target() {
		return publication_target;
	}
	public void setPublication_target(String publication_target) {
		this.publication_target = publication_target;
	}
	public String getChannel_target() {
		return channel_target;
	}
	public void setChannel_target(String channel_target) {
		this.channel_target = channel_target;
	}
	public String getDevice_target() {
		return device_target;
	}
	public void setDevice_target(String device_target) {
		this.device_target = device_target;
	}
	public String getDevice_type_target() {
		return device_type_target;
	}
	public void setDevice_type_target(String device_type_target) {
		this.device_type_target = device_type_target;
	}
	public String getVideo_target() {
		return video_target;
	}
	public void setVideo_target(String video_target) {
		this.video_target = video_target;
	}
	public String getPattern_target() {
		return pattern_target;
	}
	public void setPattern_target(String pattern_target) {
		this.pattern_target = pattern_target;
	}
	public String getQuality_target() {
		return quality_target;
	}
	public void setQuality_target(String quality_target) {
		this.quality_target = quality_target;
	}
	public String getBrand_target() {
		return brand_target;
	}
	public void setBrand_target(String brand_target) {
		this.brand_target = brand_target;
	}
	public String getCreative_show_rule() {
		return creative_show_rule;
	}
	public void setCreative_show_rule(String creative_show_rule) {
		this.creative_show_rule = creative_show_rule;
	}
	public Integer getBelong_to_advertiser() {
		return belong_to_advertiser;
	}
	public void setBelong_to_advertiser(Integer belong_to_advertiser) {
		this.belong_to_advertiser = belong_to_advertiser;
	}
	public Integer getCampaign_display_way() {
		return campaign_display_way;
	}
	public void setCampaign_display_way(Integer campaign_display_way) {
		this.campaign_display_way = campaign_display_way;
	}
	public Integer getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(Integer total_amount) {
		this.total_amount = total_amount;
	}
	public Integer getCampaign_class() {
		return campaign_class;
	}
	public void setCampaign_class(Integer campaign_class) {
		this.campaign_class = campaign_class;
	}
	public Integer getTime_target() {
		return time_target;
	}
	public void setTime_target(Integer time_target) {
		this.time_target = time_target;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}

}
