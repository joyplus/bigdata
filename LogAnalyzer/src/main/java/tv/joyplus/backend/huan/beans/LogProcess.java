package tv.joyplus.backend.huan.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class LogProcess implements Serializable{
	private String equipmentSn;
	private String equipmentKey;
	private long deviceId;
	private String deviceName;
	private String userPattern;
	private Date adDate;
	private Timestamp datetime;
	private Timestamp clientTime;
	private String operationType;
	private String operationExtra;
	private long publicationId;
	private long zoneId;
	private long campaignId;
	private long creativeId;
	private String clientIp;
	private String screenType;
	private String provinceCode;
	private String cityCode;
	private String businessId;
	public String getEquipmentSn() {
		return equipmentSn;
	}
	public void setEquipmentSn(String equipmentSn) {
		this.equipmentSn = equipmentSn;
	}
	public String getEquipmentKey() {
		return equipmentKey;
	}
	public void setEquipmentKey(String equipmentKey) {
		this.equipmentKey = equipmentKey;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getUserPattern() {
		return userPattern;
	}
	public void setUserPattern(String userPattern) {
		this.userPattern = userPattern;
	}
	public Date getAdDate() {
		return adDate;
	}
	public void setAdDate(Date adDate) {
		this.adDate = adDate;
	}
	public Timestamp getDatetime() {
		return datetime;
	}
	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
	public Timestamp getClientTime() {
		return clientTime;
	}
	public void setClientTime(Timestamp clientTime) {
		this.clientTime = clientTime;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationExtra() {
		return operationExtra;
	}
	public void setOperationExtra(String operationExtra) {
		this.operationExtra = operationExtra;
	}
	public long getPublicationId() {
		return publicationId;
	}
	public void setPublicationId(long publicationId) {
		this.publicationId = publicationId;
	}
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public long getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}
	public long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getScreenType() {
		return screenType;
	}
	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
}
