package tv.joyplus.backend.report.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.report.dao.JobResultDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.exception.ReportBaseException;
import tv.joyplus.backend.utility.CommonUtility;
import tv.joyplus.backend.utility.Const;

public class JobResultDaoImpl extends JdbcDaoSupport implements JobResultDao {

	protected Log log = LogFactory.getLog(JobResultDaoImpl.class);
	
	public void  saveJobResults(List<JobResultDto> results) {
		
		String strSQL = null;
		
		if (results == null) {
			throw new ReportBaseException(Const.EXCEPTION_NULL_PARAME, "save parameter null", null);
		}
		
		Iterator<JobResultDto> itResult = results.iterator();
		while (itResult.hasNext()) {
			JobResultDto jobResultRow = itResult.next();
			
			//获取投放信息
			String strCampaignName = null;
			String strAdvertiser = null;
			strSQL = "SELECT md_campaigns.campaign_name, ad_advertiser.name_zh FROM md_campaigns left join ad_advertiser"
					+ " on md_campaigns.belong_to_advertiser=ad_advertiser.entry_id WHERE md_campaigns.campaign_id=?";
			List<Map<String, Object>> lstCampaignResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.campaign_id);
			Iterator<Map<String, Object>> itCampaign = lstCampaignResult.iterator();
			if (itCampaign.hasNext()) {
				Map<String, Object> mapCampaign = itCampaign.next();
				strCampaignName = (String) mapCampaign.get("campaign_name");
				strAdvertiser = (String) mapCampaign.get("name_zh");
			}
			
			//获取创意信息
			String strCreativeName = null;
			String strCreativeUnitType = null;
			String strCreativeExtension = null;
			String strCreativeFile = null;
			String strCreativeSize = null;
			strSQL = "SELECT * FROM md_ad_units WHERE adv_id=?";
			List<Map<String, Object>> lstCreativeResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.adv_id);
			Iterator<Map<String, Object>> itCreative = lstCreativeResult.iterator();
			if (itCreative.hasNext()) {
				Map<String, Object> mapCreative = itCreative.next();
				strCreativeName = (String) mapCreative.get("adv_name");
				strCreativeUnitType = (String) mapCreative.get("creative_unit_type");
				String strCreativeType = (String) mapCreative.get("adv_type");
				if (!Const.CREATIVE_TYPE_OPEN.equals(strCreativeUnitType)) {
					strCreativeFile = (String) mapCreative.get("adv_creative_url");
				} else {
					if (Const.CREATIVE_TYPE_VIDEO.equals(strCreativeType)) {
						strCreativeFile = (String) mapCreative.get("adv_creative_url_3");
					} else if (Const.CREATIVE_TYPE_ZIP.equals(strCreativeType)) {
						strCreativeFile = (String) mapCreative.get("adv_creative_url_2");
					}
				}
				
				if (strCreativeFile != null) {
					strCreativeExtension = strCreativeFile.substring(strCreativeFile.lastIndexOf(".") + 1);
				}
				
				if (Const.CREATIVE_TYPE_VIDEOANDZIP.equals(strCreativeType)) {
					strCreativeFile = (String) mapCreative.get("adv_creative_url_2");
					if (strCreativeFile != null) {
						strCreativeExtension = strCreativeFile.substring(strCreativeFile.lastIndexOf(".") + 1);
					}
					strCreativeFile = (String) mapCreative.get("adv_creative_url_3");
					if (strCreativeFile != null) {
						strCreativeExtension = strCreativeExtension + "、" + strCreativeFile.substring(strCreativeFile.lastIndexOf(".") + 1);
					}
				}
				String width = String.valueOf(mapCreative.get("adv_width")) ;
				String height = String.valueOf(mapCreative.get("adv_height")) ;
				if(!CommonUtility.isEmptyString(width) 
						&& !CommonUtility.isEmptyString(height)
						&&!"null".equalsIgnoreCase(width)
						&&!"null".equalsIgnoreCase(height)){
					strCreativeSize =  width + "x" + height;
				}
			}
			
			//获取媒体信息
			String strPublicationName = null;
			strSQL = "SELECT * FROM md_publications WHERE inv_id=?";
			List<Map<String, Object>> lstPublicationResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.publication_id);
			Iterator<Map<String, Object>> itPublication = lstPublicationResult.iterator();
			if (itPublication.hasNext()) {
				strPublicationName = (String) itPublication.next().get("inv_name");
			}
			
			//获取广告位信息
			String strZoneName = null;
			String strZoneType = null;
			String strZoneSize = null;
			strSQL = "SELECT * FROM md_zones WHERE entry_id=?";
			List<Map<String, Object>> lstZoneResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.zone_id);
			Iterator<Map<String, Object>> itZone = lstZoneResult.iterator();
			if (itZone.hasNext()) {
				Map<String, Object> mapZone = itZone.next();
				strZoneName = (String) mapZone.get("zone_name");
				strZoneType = (String) mapZone.get("zone_type");
				String width = String.valueOf(mapZone.get("zone_width")) ;
				String height = String.valueOf(mapZone.get("zone_height")) ;
				if(!CommonUtility.isEmptyString(width) 
						&& !CommonUtility.isEmptyString(height)
						&&!"null".equalsIgnoreCase(width)
						&&!"null".equalsIgnoreCase(height)){
					strZoneSize =  width + "x" + height;
				}
//				strZoneSize = (String) mapZone.get("zone_width") + "x" + (String) mapZone.get("zone_height");
			}
			
			//获取地域信息
			String strRegionName = null;
			strSQL = "SELECT * FROM md_regional_targeting WHERE targeting_code=?";
			List<Map<String, Object>> lstRegionResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.region_code);
			Iterator<Map<String, Object>> itRegion = lstRegionResult.iterator();
			if (itRegion.hasNext()) {
				strRegionName = (String) itRegion.next().get("region_name");
			}
			
			//获取设备信息
			int iDeviceId = 0;
			String strDeviceBrand = null;
			String strDeviceMovement = null;
			strSQL = "SELECT md_devices.device_id, md_devices.device_movement, md_lov.value FROM md_devices join md_lov "
					+ "on md_devices.device_brands=md_lov.code WHERE device_name=?";
			List<Map<String, Object>> lstDeviceResult = getJdbcTemplate().queryForList(strSQL, jobResultRow.device_name);
			Iterator<Map<String, Object>> itDevice = lstDeviceResult.iterator();
			if (itDevice.hasNext()) {
				Map<String, Object> mapDevice = itDevice.next();
				iDeviceId = (Integer) mapDevice.get("device_id");
				strDeviceBrand = (String) mapDevice.get("value");
				strDeviceMovement = (String) mapDevice.get("device_movement");
			}
			
			//插入数据库
			strSQL = "INSERT INTO ad_job_result(job_id,date_start,date_end,hour,campaign_id,campaign_name,adv_id,adv_name,publication_id,"
					+ "publication_name,zone_id,zone_name,advistiser_id,advistiser_name,request,impression,uv,quality_id,quality_name,"
					+ "region_code,region_name,device_id,device_brand,device_name,device_movement,creative_unit_type,creative_extension,"
					+ "creative_size,zone_type,zone_size,frequency)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			getJdbcTemplate().update(strSQL, jobResultRow.job_id, jobResultRow.date_start, jobResultRow.date_end, jobResultRow.hour,
					jobResultRow.campaign_id, strCampaignName, jobResultRow.adv_id, strCreativeName, jobResultRow.publication_id, strPublicationName,
					jobResultRow.zone_id, strZoneName, jobResultRow.advistiser_id, strAdvertiser, jobResultRow.request, jobResultRow.impression,
					jobResultRow.uv, 0, "", jobResultRow.region_code, strRegionName, iDeviceId, strDeviceBrand, jobResultRow.device_name,
					strDeviceMovement, strCreativeUnitType, strCreativeExtension, strCreativeSize, strZoneType, strZoneSize, 
					jobResultRow.frequency);
		}
	}

	public void updateReportStatus(String reportId, int status) {
		String strSQL = "UPDATE md_customize_report set status=? where report_id=?";
		getJdbcTemplate().update(strSQL, status,reportId);
	}

}
