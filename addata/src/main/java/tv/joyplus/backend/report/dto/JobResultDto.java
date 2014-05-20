package tv.joyplus.backend.report.dto;

public class JobResultDto {
	public int id;
	public int job_id;
	public String date_start;
	public String date_end;
	public int campaign_id;
	public String campaign_name;
	public int adv_id;
	public String adv_name;
	public int publication_id;
	public String publication_name;
	public int zone_id;
	public String zone_name;
	public int advistiser_id;
	public String advistiser_name;
	public int request;
	public int uv;
	public int quality_id;
	public String quality_name;
	public String region_code;
	public String region_name;
	public int device_id;
	public String device_brand;
	public String device_name;
	public String device_movement;
	public String creative_unit_type;
	public String creative_extension;
	public String creative_size;
	public String zone_type;
	public String zone_size;
	public int frequency;
	private int impression;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJob_id() {
		return job_id;
	}
	public void setJob_id(int job_id) {
		this.job_id = job_id;
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
	public int getCampaign_id() {
		return campaign_id;
	}
	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}
	public String getCampaign_name() {
		return campaign_name;
	}
	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}
	public int getAdv_id() {
		return adv_id;
	}
	public void setAdv_id(int adv_id) {
		this.adv_id = adv_id;
	}
	public String getAdv_name() {
		return adv_name;
	}
	public void setAdv_name(String adv_name) {
		this.adv_name = adv_name;
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
	public int getAdvistiser_id() {
		return advistiser_id;
	}
	public void setAdvistiser_id(int advistiser_id) {
		this.advistiser_id = advistiser_id;
	}
	public String getAdvistiser_name() {
		return advistiser_name;
	}
	public void setAdvistiser_name(String advistiser_name) {
		this.advistiser_name = advistiser_name;
	}
	public int getRequest() {
		return request;
	}
	public void setRequest(int request) {
		this.request = request;
	}
	public int getUv() {
		return uv;
	}
	public void setUv(int uv) {
		this.uv = uv;
	}
	public int getQuality_id() {
		return quality_id;
	}
	public void setQuality_id(int quality_id) {
		this.quality_id = quality_id;
	}
	public String getQuality_name() {
		return quality_name;
	}
	public void setQuality_name(String quality_name) {
		this.quality_name = quality_name;
	}
	public String getRegion_code() {
		return region_code;
	}
	public void setRegion_code(String region_code) {
		this.region_code = region_code;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getDevice_brand() {
		return device_brand;
	}
	public void setDevice_brand(String device_brand) {
		this.device_brand = device_brand;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_movement() {
		return device_movement;
	}
	public void setDevice_movement(String device_movement) {
		this.device_movement = device_movement;
	}
	public String getCreative_unit_type() {
		return creative_unit_type;
	}
	public void setCreative_unit_type(String creative_unit_type) {
		this.creative_unit_type = creative_unit_type;
	}
	public String getCreative_extension() {
		return creative_extension;
	}
	public void setCreative_extension(String creative_extension) {
		this.creative_extension = creative_extension;
	}
	public String getCreative_size() {
		return creative_size;
	}
	public void setCreative_size(String creative_size) {
		this.creative_size = creative_size;
	}
	public String getZone_type() {
		return zone_type;
	}
	public void setZone_type(String zone_type) {
		this.zone_type = zone_type;
	}
	public String getZone_size() {
		return zone_size;
	}
	public void setZone_size(String zone_size) {
		this.zone_size = zone_size;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public int getImpression() {
		return impression;
	}
	public void setImpression(int impression) {
		this.impression = impression;
	}
	@Override
	public String toString() {
		return "JobResultDto [id=" + id + ", job_id=" + job_id
				+ ", date_start=" + date_start + ", date_end=" + date_end
				+ ", campaign_id=" + campaign_id + ", campaign_name="
				+ campaign_name + ", adv_id=" + adv_id + ", adv_name="
				+ adv_name + ", publication_id=" + publication_id
				+ ", publication_name=" + publication_name + ", zone_id="
				+ zone_id + ", zone_name=" + zone_name + ", advistiser_id="
				+ advistiser_id + ", advistiser_name=" + advistiser_name
				+ ", request=" + request + ", uv=" + uv + ", quality_id="
				+ quality_id + ", quality_name=" + quality_name
				+ ", region_code=" + region_code + ", region_name="
				+ region_name + ", device_id=" + device_id + ", device_brand="
				+ device_brand + ", device_name=" + device_name
				+ ", device_movement=" + device_movement
				+ ", creative_unit_type=" + creative_unit_type
				+ ", creative_extension=" + creative_extension
				+ ", creative_size=" + creative_size + ", zone_type="
				+ zone_type + ", zone_size=" + zone_size + ", frequency="
				+ frequency + ", impression=" + impression + "]";
	}	

}