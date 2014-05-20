package tv.joyplus.backend.report.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;
import tv.joyplus.backend.report.dto.ParameterDto.Type;
import tv.joyplus.backend.utility.CommonUtility;

public class ProcessDaoImpl extends JdbcDaoSupport implements ProcessDao {

//	Logger log = Logger.getLogger(ProcessDaoImpl.class);
	
	public List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		if(parameterDto==null){
			return null;
		}
		if(parameterDto.getType().equals(Type.CAMPAIGN.toString())){
			return queryByCampaignId(parameterDto);
		}else if(parameterDto.getType().equals(Type.UNIT.toString())){
			return queryByUnitId(parameterDto);
		}else if(parameterDto.getType().equals(Type.PUBLICATION.toString())){
			return queryByPublicationId(parameterDto);
		}else if(parameterDto.getType().equals(Type.ZONE.toString())){
			return queryByZoneId(parameterDto);
		}else if(parameterDto.getType().equals(Type.MONITOR.toString())){
			return queryByMonitor(parameterDto);
		}else if(parameterDto.getType().equals(Type.MONITORUNIT.toString())){
			return queryByMonitorunit(parameterDto);
		}else if(parameterDto.getType().equals(Type.LOCATION.toString())){
			return queryByLocation(parameterDto);
		}else{
//			log.error("unkown type");
			System.err.println("unkown type");
			return null;
		}
			
	}
	
	private List<JobResultDto> queryByCampaignId(ParameterDto parameterDto){
		System.out.println("queryByCampaignId");
		List<String> resouces = parameterDto.getDataResource();
		StringBuilder sqlBuilder = new StringBuilder();
		if(parameterDto.getFrequency()<0){
			String condition = getConditionFromDataResource(resouces);
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, false, true));
			sqlBuilder.append(" from md_device_request_log ");
			if(condition == null){
				sqlBuilder.append("where 1 ");
			}else{
				sqlBuilder.append("where campaign_id in ( ").append(condition).append(" ) ");
			}
			String groupBy = getGroupByFromList(parameterDto);
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilder.append(" group by ").append(groupBy);
				if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_part");
				}
			}
			
		}else if(parameterDto.getFrequency()>10){
			StringBuilder sqlBuilderChild1 = new StringBuilder();
			sqlBuilderChild1.append("select ");
			sqlBuilderChild1.append(getDataFeild(parameterDto, true, true));
			sqlBuilderChild1.append(", if( count( * ) >=10, 10, count( * ) ) as frequency, count( * ) as impression_count ");
			sqlBuilderChild1.append(" from md_device_request_log ");
			String condition = getConditionFromDataResource(resouces);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(" where campaign_id in ( ").append(condition).append(" ) ");
			}
			String groupBy = getGroupByFromList(parameterDto);
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilderChild1.append(" group by ").append(groupBy);
				sqlBuilderChild1.append(", equipment_key");
				if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_part");
				}
			}
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, true, false));
			sqlBuilder.append(", frequency, count(*) as uv, sum(impression_count) as impression ");
			sqlBuilder.append(" from (");
			sqlBuilder.append(sqlBuilderChild1.toString());
			sqlBuilder.append(" )child1 ");
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilder.append(" group by ").append(groupBy);
				sqlBuilder.append(", frequency");
			}
		}else{
			StringBuilder sqlBuilderChild1 = new StringBuilder();
			sqlBuilderChild1.append("select ");
			sqlBuilderChild1.append(getDataFeild(parameterDto, true, true));
			sqlBuilderChild1.append(",count( * ) as frequency ");
			sqlBuilderChild1.append(" from md_device_request_log ");
			String condition = getConditionFromDataResource(resouces);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(" where campaign_id in ( ").append(condition).append(" ) ");
			}
			String groupBy = getGroupByFromList(parameterDto);
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilderChild1.append(" group by ").append(groupBy);
				sqlBuilderChild1.append(", equipment_key");
				if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_part");
				}
			}
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, true, false));
			sqlBuilder.append(", frequency, count(*) as uv, sum(frequency) as impression ");
			sqlBuilder.append(" from (");
			sqlBuilder.append(sqlBuilderChild1.toString());
			sqlBuilder.append(" )child1 ");
			sqlBuilder.append(" where frequency <= '").append(parameterDto.getFrequency()).append("' ");
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilder.append(" group by ").append(groupBy);
				sqlBuilder.append(", frequency");
			}
		}
		System.out.println(sqlBuilder.toString());
		String sql = sqlBuilder.toString();
		List<JobResultDto> jobResultDtos = new ArrayList<JobResultDto>();
		List rows = getJdbcTemplate().queryForList(sql);
		System.out.println("result size = \t" + rows.size());
		Iterator it = rows.iterator();
		while(it.hasNext()){
			Map jobResultMap = (Map) it.next();  
			JobResultDto jobResultDto = new JobResultDto();
			jobResultDto.setJob_id(Integer.valueOf(parameterDto.getReportId()));
			System.out.println(jobResultMap.get("campaign_id") 
					+ "\t" + jobResultMap.get("publication_id")
					+ "\t" + jobResultMap.get("zone_id")
					+ "\t" + jobResultMap.get("operation_type_tmp")
					+ "\t" + jobResultMap.get("frequency")
					+ "\t" + jobResultMap.get("province_code")
					+ "\t" + jobResultMap.get("city_code")
					+ "\t" + jobResultMap.get("device_name")
					+ "\t" + jobResultMap.get("uv")
					+ "\t" + jobResultMap.get("time_part")
					+ "\t" + jobResultMap.get("impression"));
			if(jobResultMap.containsKey("campaign_id")){
				jobResultDto.setCampaign_id((Integer) jobResultMap.get("campaign_id"));
			}
			if(jobResultMap.containsKey("publication_id")){
				jobResultDto.setPublication_id((Integer) jobResultMap.get("publication_id"));
			}
			if(jobResultMap.containsKey("zone_id")){
				jobResultDto.setZone_id((Integer) jobResultMap.get("zone_id"));
			}
			if(jobResultMap.containsKey("frequency")){
				jobResultDto.setFrequency((Integer) jobResultMap.get("frequency"));
			}
			if(jobResultMap.containsKey("device_name")){
				jobResultDto.setDevice_brand((String) jobResultMap.get("device_name"));
			}
			if(jobResultMap.containsKey("uv")){
				jobResultDto.setUv(((Long) jobResultMap.get("uv")).intValue());
			}
			if(jobResultMap.containsKey("impression")){
				jobResultDto.setImpression(((Long) jobResultMap.get("impression")).intValue());
			}
			if(jobResultMap.containsKey("device_name")){
				jobResultDto.setDevice_brand((String) jobResultMap.get("device_name"));
			}
			if(jobResultMap.containsKey("province_code")){
				jobResultDto.setRegion_code((String) jobResultMap.get("province_code"));
			}
			if(jobResultMap.containsKey("city_code")){
				jobResultDto.setRegion_code((String) jobResultMap.get("city_code"));
			}
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try{
				if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyyww");
					calendar.setTime(format.parse((String)jobResultMap.get("time_part")));
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					jobResultDto.setDate_start(dateFormat.format(calendar.getTime()));
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					jobResultDto.setDate_end(dateFormat.format(calendar.getTime()));
				}else if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					jobResultDto.setDate_start(dateFormat.format((String)jobResultMap.get("time_part")));
					jobResultDto.setDate_end(dateFormat.format((String)jobResultMap.get("time_part")));
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					calendar.setTime(format.parse((String)jobResultMap.get("time_part")));
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					jobResultDto.setDate_start(dateFormat.format(calendar.getTime()));
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					jobResultDto.setDate_end(dateFormat.format(calendar.getTime()));
				}
			}catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(jobResultDto.toString());
			jobResultDtos.add(jobResultDto);
		}
		return jobResultDtos;
	}
	private List<JobResultDto> queryByUnitId(ParameterDto parameterDto){
		return null;
	}
	private List<JobResultDto> queryByPublicationId(ParameterDto parameterDto){
		return null;
	}
	private List<JobResultDto> queryByZoneId(ParameterDto parameterDto){
		return null;
	}
	private List<JobResultDto> queryByMonitor(ParameterDto parameterDto){
		return null;
	}
	private List<JobResultDto> queryByMonitorunit(ParameterDto parameterDto){
		return null;
	}
	private List<JobResultDto> queryByLocation(ParameterDto parameterDto){
		return null;
	}
	
	private String getConditionFromDataResource(List<String> resouces){
		if(resouces==null || resouces.size()==0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < resouces.size(); i++) {
			if(i==0){
				sb.append("'");
			}else{
				sb.append(" , '");
			}
			sb.append(resouces.get(i));
			sb.append("'");
		}
		return sb.toString();
	}
	private String getGroupByFromList(ParameterDto parameterDto){
		
		List<String> groupByList = parameterDto.getGroupby();
		if(groupByList==null || groupByList.size()==0){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < groupByList.size(); i++) {
			if(!"frequency".equalsIgnoreCase(groupByList.get(i))){
				sb = addFiled(sb, groupByList.get(i));
			}
		}
		String groupByFromPrama = sb.toString();
		return replace(groupByFromPrama);
	}
	/**
	 * 
	 * @param parameterDto
	 * @param hasFrequency  whether count the impression
	 * @param hasTimePart
	 * @return
	 */
	private String getDataFeild(ParameterDto parameterDto, boolean hasFrequency, boolean hasTimePart){
		StringBuilder sb = new StringBuilder();
		String groupBy = getGroupByFromList(parameterDto);
		if(!CommonUtility.isEmptyString(groupBy)){
			sb.append(groupBy);
		}
		StringBuilder sb_item = new StringBuilder();
		List<String> itemList =  parameterDto.getItems();
		if(itemList!=null && itemList.size()>0){
			for (int i = 0; i < itemList.size(); i++) {
				if("impression".equalsIgnoreCase(itemList.get(i))){
					if(!hasFrequency){
						sb_item = addFiled(sb_item, "count(*) as impression");
					}
				}else if(("uv").equalsIgnoreCase(itemList.get(i))){
					if(!hasFrequency){
						sb_item = addFiled(sb_item, "count(distinct equipment_key) as uv");
					}
				}else{
					if(!"campaign_owner".equalsIgnoreCase(itemList.get(i)) && !(sb.indexOf(itemList.get(i))>0)){
						sb_item = addFiled(sb_item, itemList.get(i));
					}
				}
			}
		}
		sb = addFiled(sb, replace(sb_item.toString()));
		if(hasTimePart){
			if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sb = addFiled(sb, "DATE_FORMAT(datetime,'%Y%u') as time_part");
			}else if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sb = addFiled(sb, "DATE_FORMAT(datetime,'%Y%-%m-%d') as time_part");
			}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sb = addFiled(sb, "DATE_FORMAT(datetime,'%Y-%m') as time_part");
			}
		}else{
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sb = addFiled(sb, "time_part");
			}
		}
		
		String groupByFromPrama = sb.toString();
		if(CommonUtility.isEmptyString(groupByFromPrama)){
			groupByFromPrama = " * ";
		}
		return groupByFromPrama;
	}
	
	
	private StringBuilder addFiled(StringBuilder sb, String feild){
		if(!CommonUtility.isEmptyString(sb.toString())){
			sb.append(" , ");
		}
		return sb.append(" ").append(feild).append(" ");
	}
	
	private String replace(String str){
		return str.replace("campaign_name", "campaign_id")
		.replace("device_brands", "device_name")
		.replace("zone_name", "zone_id")
		.replace("province", "province_code")
		.replace("city", "city_code")
		.replace("adv_name", "creative_id")
		.replace("inv_name", "publication_id");
	}
}
