package tv.joyplus.backend.report.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;
import tv.joyplus.backend.report.dto.ParameterDto.Type;
import tv.joyplus.backend.report.exception.ReportBaseException;
import tv.joyplus.backend.utility.CommonUtility;
import tv.joyplus.backend.utility.Const;

public class ProcessDaoImpl extends JdbcDaoSupport implements ProcessDao {

	protected Log log = LogFactory.getLog(ProcessDaoImpl.class);

	public List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		if(parameterDto==null){
			throw new ReportBaseException(Const.EXCEPTION_NULL_PARAME, "query parameter null", "");
		}
		if(parameterDto.getType()!=null){
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
				throw new ReportBaseException(Const.EXCEPTION_UNKOWN_TYPE, "unkown type  parameter ", parameterDto.getType());
			}
			
		}else{
			throw new ReportBaseException(Const.EXCEPTION_UNKOWN_TYPE, "unkown type  parameter ", "parameterDto.getType() is null");
		}
	}
	
	private List<JobResultDto> queryData(Type type, ParameterDto parameterDto){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sqlBuilder = new StringBuilder();
		if(parameterDto.getFrequency()<0){
			String condition = getConditionFromParameter(type,parameterDto);
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, false, true));
			if(type == Type.PUBLICATION || type == Type.ZONE){
				sqlBuilder.append(", if( operation_type='001', '002', operation_type ) as operation_type_temp ");
			}
			sqlBuilder.append(" from md_device_request_log ");
			if(condition == null){
				sqlBuilder.append(" where 1 ");
			}else{
				sqlBuilder.append(condition);
			}
			
			String groupBy = getGroupByFromList(parameterDto);
			if(type == Type.PUBLICATION || type == Type.ZONE){
				sqlBuilder.append(" and operation_type in ('001','002', '003') ");
				sqlBuilder.append(" group by ").append(groupBy);
				sqlBuilder = addFiled(sqlBuilder, "operation_type_temp");
				if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_part");
				}
			}else{
				sqlBuilder.append(" and operation_type = '003' ");
				if(!CommonUtility.isEmptyString(groupBy)){
					sqlBuilder.append(" group by ").append(groupBy);
					if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder = addFiled(sqlBuilder, "time_part");
					}
				}else{
					if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder.append(" group by time_part");
					}
				}
			}
			
		}else if(parameterDto.getFrequency()>10){
			StringBuilder sqlBuilderChild1 = new StringBuilder();
			sqlBuilderChild1.append("select ");
			sqlBuilderChild1.append(getDataFeild(parameterDto, true, true));
			sqlBuilderChild1.append(", if( count( * ) >10, 11, count( * ) ) as frequency, count( * ) as impression_count ");
			sqlBuilderChild1.append(" from md_device_request_log ");
			String condition = getConditionFromParameter(type,parameterDto);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(condition);
			}
			sqlBuilderChild1.append(" and operation_type = '003' ");
			String groupBy = getGroupByFromList(parameterDto);
			sqlBuilderChild1.append(" group by ").append(groupBy);
			sqlBuilderChild1 = addFiled(sqlBuilderChild1, "equipment_key");
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_part");
			}
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, true, false));
			sqlBuilder.append(", frequency, count(*) as uv, sum(impression_count) as impression ");
			sqlBuilder.append(" from (");
			sqlBuilder.append(sqlBuilderChild1.toString());
			sqlBuilder.append(" )child1 ");
			sqlBuilder.append(" where frequency > '10' ");
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
			String condition = getConditionFromParameter(type,parameterDto);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(condition);
			}
			sqlBuilderChild1.append(" and operation_type = '003' ");
			String groupBy = getGroupByFromList(parameterDto);
			sqlBuilderChild1.append(" group by ").append(groupBy);
			sqlBuilderChild1 = addFiled(sqlBuilderChild1, "equipment_key");
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_part");
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
		log.info(sqlBuilder.toString());
		String sql = sqlBuilder.toString();
		List<JobResultDto> jobResultDtos = new ArrayList<JobResultDto>();
		List<Map<String, Object>> rows = null;
		try {
			rows = getJdbcTemplate().queryForList(sql);
		} catch (DataAccessException e) {
			// TODO: handle exception
			throw new ReportBaseException(Const.EXCEPTION_BADSQL, "query faile", e.getMessage());
		}
		
		log.debug("result size = \t" + rows.size());
		Iterator<Map<String, Object>> it = rows.iterator();
		while(it.hasNext()){
			Map<String, Object> jobResultMap = it.next();  
			JobResultDto jobResultDto = new JobResultDto();
			jobResultDto.setJob_id(Integer.valueOf(parameterDto.getReportId()));
			if(jobResultMap.containsKey("campaign_id")){
				jobResultDto.setCampaign_id(Integer.valueOf(String.valueOf(jobResultMap.get("campaign_id"))));
			}
			if(jobResultMap.containsKey("publication_id")){
				jobResultDto.setPublication_id(Integer.valueOf(String.valueOf(jobResultMap.get("publication_id"))));
			}
			if(jobResultMap.containsKey("zone_id")){
				jobResultDto.setZone_id(Integer.valueOf(String.valueOf(jobResultMap.get("zone_id"))));
			}
			if(jobResultMap.containsKey("creative_id")){
				jobResultDto.setAdv_id(Integer.valueOf(String.valueOf(jobResultMap.get("creative_id"))));
			}
			if(jobResultMap.containsKey("frequency")){
				jobResultDto.setFrequency(Integer.valueOf(String.valueOf(jobResultMap.get("frequency"))));
			}
			if(jobResultMap.containsKey("device_name")){
				jobResultDto.setDevice_name((String) jobResultMap.get("device_name"));
			}
			if(jobResultMap.containsKey("uv")){
				jobResultDto.setUv(Integer.valueOf(String.valueOf(jobResultMap.get("uv"))));
				
			}
			if(jobResultMap.containsKey("impression")){
				jobResultDto.setImpression(Integer.valueOf(String.valueOf(jobResultMap.get("impression"))));
			}
			if(jobResultMap.containsKey("province_code")){
				jobResultDto.setRegion_code((String) jobResultMap.get("province_code"));
			}
			if(jobResultMap.containsKey("city_code")){
				jobResultDto.setRegion_code((String) jobResultMap.get("city_code"));
			}
			if(jobResultMap.containsKey("operation_type_temp")){
				jobResultDto.setOperation_type((String) jobResultMap.get("operation_type_temp"));
			}
			Calendar calendar = Calendar.getInstance();
			try{
				if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyyww");
					calendar.setTime(format.parse((String)jobResultMap.get("time_part")));
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					jobResultDto.setDate_start(dateFormat.format((parameterDto.getDateRange()[0].getTime()>=calendar.getTime().getTime())? parameterDto.getDateRange()[0] : calendar.getTime()));
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					jobResultDto.setDate_end(dateFormat.format((parameterDto.getDateRange()[1].getTime()<=calendar.getTime().getTime())? parameterDto.getDateRange()[1] : calendar.getTime()));
				}else if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					jobResultDto.setDate_start((String)jobResultMap.get("time_part"));
					jobResultDto.setDate_end((String)jobResultMap.get("time_part"));
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					calendar.setTime(format.parse((String)jobResultMap.get("time_part")));
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					jobResultDto.setDate_start(dateFormat.format((parameterDto.getDateRange()[0].getTime()>=calendar.getTime().getTime())? parameterDto.getDateRange()[0] : calendar.getTime()));
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					jobResultDto.setDate_end(dateFormat.format(parameterDto.getDateRange()[1].getTime()<=calendar.getTime().getTime()? parameterDto.getDateRange()[1] : calendar.getTime()));
				}else{
					jobResultDto.setDate_start(dateFormat.format(parameterDto.getDateRange()[0]));
					jobResultDto.setDate_end(dateFormat.format(parameterDto.getDateRange()[1]));
				}
			}catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				log.error(e.getMessage());
			}
			jobResultDtos.add(jobResultDto);
		}
		if(type == Type.PUBLICATION || type == Type.ZONE){
			jobResultDtos = mergeRequsetAndImpression(jobResultDtos);
		}
		log.debug("return size = \t" + rows.size());
		for(JobResultDto jobResultDto : jobResultDtos){
			log.debug(jobResultDto.toString());
		}
		return jobResultDtos;
	}
	
	private List<JobResultDto> mergeRequsetAndImpression(List<JobResultDto> jobResultDtos) {
		// TODO Auto-generated method stub
		log.debug("mergeRequsetAndImpression");
		Iterator<JobResultDto> iterator = jobResultDtos.iterator();
		List<JobResultDto> jobresults = new ArrayList<JobResultDto>();
		JobResultDto jobresult = null;
		while(iterator.hasNext()){
			if(jobresult == null){
				jobresult = iterator.next();
			}else{
				JobResultDto jobresult_1 = iterator.next();
				if(isSameItemExceptOperationType(jobresult, jobresult_1)){
					if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
						jobresult_1.setRequest(jobresult.getImpression());
						jobresult_1.setUv(jobresult.getUv());
						jobresult = jobresult_1;
					}else{
						jobresult.setRequest(jobresult_1.getImpression());
					}
				}else{
					if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
						jobresult.setRequest(jobresult.getImpression());
						jobresult.setUv(0);
						jobresult.setImpression(0);
					}
					jobresults.add(jobresult);
					jobresult = jobresult_1;
				}
			}
		}
		if(jobresult!=null){
			if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
				jobresult.setRequest(jobresult.getImpression());
				jobresult.setUv(0);
				jobresult.setImpression(0);
			}
			jobresults.add(jobresult);
		}
		return jobresults;
	}

	private List<JobResultDto> queryByCampaignId(ParameterDto parameterDto){
		log.debug("queryByCampaignId");
		return queryData(ParameterDto.Type.CAMPAIGN, parameterDto);
	}
	private List<JobResultDto> queryByUnitId(ParameterDto parameterDto){
		log.debug("queryByUnitId");
		return queryData(ParameterDto.Type.UNIT, parameterDto);
	}
	private List<JobResultDto> queryByPublicationId(ParameterDto parameterDto){
		log.debug("queryByPublicationId");
		return queryData(ParameterDto.Type.PUBLICATION, parameterDto);
	}
	private List<JobResultDto> queryByZoneId(ParameterDto parameterDto){
		log.debug("queryByZoneId");
		return queryData(ParameterDto.Type.ZONE, parameterDto);
	}
	private List<JobResultDto> queryByMonitor(ParameterDto parameterDto){
		log.debug("queryByMonitor");
		return queryData(ParameterDto.Type.MONITOR, parameterDto);
	}
	private List<JobResultDto> queryByMonitorunit(ParameterDto parameterDto){
		log.debug("queryByMonitorunit");
		return queryData(ParameterDto.Type.MONITORUNIT, parameterDto);
	}
	private List<JobResultDto> queryByLocation(ParameterDto parameterDto){
		log.debug("queryByLocation");
		return queryData(ParameterDto.Type.LOCATION, parameterDto);
	}
	
	private String getConditionFromParameter(Type type, ParameterDto parameterDto){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<String> resouces = parameterDto.getDataResource();
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
		String condition = sb.toString();
		StringBuilder conditionBuilder = new StringBuilder();
		switch (type) {
			case CAMPAIGN:
				conditionBuilder.append(" where campaign_id in ( ").append(condition).append(" ) ");
				break;
			case PUBLICATION:
				conditionBuilder.append(" where publication_id in ( ").append(condition).append(" ) ");
				break;
			case UNIT:
				conditionBuilder.append(" where creative_id in ( ").append(condition).append(" ) ");
				break;
			case ZONE:
				conditionBuilder.append(" where zone_id in ( ").append(condition).append(" ) ");
				break;
			case LOCATION:
				conditionBuilder.append(" where city_code in ( ").append(condition).append(" ) ");
				break;
			case MONITOR:
				conditionBuilder.append(" where campaign_id in ( ").append(condition).append(" ) ");
				break;
			case MONITORUNIT:
				conditionBuilder.append(" where creative_id in ( ").append(condition).append(" ) ");
				break;
			default:
				conditionBuilder.append(" where 1 ");
				break;
		}
		if(parameterDto.getDateRange().length>=2){
			conditionBuilder.append(" and ad_date >= '")
				.append(dateFormat.format(parameterDto.getDateRange()[0]))
				.append(" ' and ad_date <= ' ")
				.append(dateFormat.format(parameterDto.getDateRange()[1]))
				.append("' ");
		}
		return conditionBuilder.toString();
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
	 * @param hasFrequency  
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
					String feild = replace(itemList.get(i));
					if(!"request".equalsIgnoreCase(feild) 
							&& !(sb.indexOf(feild)>0)
							&& !(sb_item.indexOf(feild)>0)){
						sb_item = addFiled(sb_item, feild);
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
		if(CommonUtility.isEmptyString(feild)){
			return sb;
		}
		if(!CommonUtility.isEmptyString(sb.toString())){
			sb.append(" , ");
		}
		return sb.append(" ").append(feild).append(" ");
	}
	
	private String replace(String str){
		return str.replace("campaign_name", "campaign_id")
		.replace("campaign_owner", "campaign_id")
		.replace("device_brands", "device_name")
		.replace("zone_name", "zone_id")
		.replace("zone_type", "zone_id")
		.replace("zone_size", "zone_id")
		.replace("province", "province_code")
		.replace("city", "city_code")
		.replace("adv_name", "creative_id")
		.replace("adv_id", "creative_id")
		.replace("adv_size", "creative_id")
		.replace("adv_type", "creative_id")
		.replace("adv_ext", "creative_id")
		.replace("inv_id", "publication_id")
		.replace("inv_name", "publication_id");
	}
	
	private boolean isSameItemExceptOperationType(JobResultDto jobResultDtoA, JobResultDto jobResultDtoB){
		
		if(jobResultDtoA == null || jobResultDtoB ==null){
			return false;
		}
		
		boolean isIdsEquals = false;
		if(jobResultDtoA.getCampaign_id() == jobResultDtoB.getCampaign_id() 
				&& jobResultDtoA.getAdv_id() == jobResultDtoB.getAdv_id()
				&& jobResultDtoA.getPublication_id() == jobResultDtoB.getPublication_id()
				&& jobResultDtoA.getZone_id() == jobResultDtoB.getZone_id()){
			isIdsEquals = true;
		}
		boolean isFrequencyEquals = (jobResultDtoA.getFrequency() == jobResultDtoB.getFrequency());
		boolean isDateStartEquals = false;
		if(jobResultDtoA.getDate_start()!=null){
			if(jobResultDtoA.getDate_start().equals(jobResultDtoB.getDate_start())){
				isDateStartEquals = true;
			}
		}else{
			if(jobResultDtoB.getDate_start() == null){
				isDateStartEquals = true;
			}
		}
		boolean isDateEndEquals = false;
		if(jobResultDtoA.getDate_end()!=null){
			if(jobResultDtoA.getDate_end().equals(jobResultDtoB.getDate_end())){
				isDateEndEquals = true;
			}
		}else{
			if(jobResultDtoB.getDate_end() == null){
				isDateEndEquals = true;
			}
		}
		
		boolean isRegion_code = false;
		if(jobResultDtoA.getRegion_code()!=null){
			if(jobResultDtoA.getRegion_code().equals(jobResultDtoB.getRegion_code())){
				isDateStartEquals = true;
			}
		}else{
			if(jobResultDtoB.getRegion_code() == null){
				isRegion_code = true;
			}
		}
		return isIdsEquals && isFrequencyEquals && isDateStartEquals && isDateEndEquals && isRegion_code;
	}
	
}
