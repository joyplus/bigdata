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

public class ProcessDaoImplWithHive extends JdbcDaoSupport implements ProcessDao {

	protected Log log = LogFactory.getLog(ProcessDaoImplWithHive.class);
	private String business_id;
	private String hive_table_name;

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	
	public String getHive_table_name() {
		return hive_table_name;
	}

	public void setHive_table_name(String hive_table_name) {
		this.hive_table_name = hive_table_name;
	}

	public List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		log.info(business_id);
		if(parameterDto==null){
			throw new ReportBaseException(Const.EXCEPTION_NULL_PARAME, "query parameter null", null);
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
				throw new ReportBaseException(Const.EXCEPTION_UNKOWN_TYPE, "unkown type  parameter" + parameterDto.getType(), null);
			}
			
		}else{
			throw new ReportBaseException(Const.EXCEPTION_UNKOWN_TYPE, "unkown type  parameter , parameterDto.getType() is null", null);
		}
	}
	
	private List<JobResultDto> queryData(Type type, ParameterDto parameterDto){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder sqlBuilder = new StringBuilder();
		if(parameterDto.getFrequency()<0){
			String condition = getConditionFromParameter(type,parameterDto);
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, false));
			sqlBuilder.append(" from ");
			if((type == Type.PUBLICATION || type == Type.ZONE) && !ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilder.append("(select * ,if(operation_type='001', '002', operation_type) as operation_type_temp, "
						+ "year(dt) as time_year, month(dt) as time_month, day(dt) as time_day, weekofyear(dt) as time_week "
						+ "from ").append(hive_table_name).append(")child1 ");
			}else if((type == Type.PUBLICATION || type == Type.ZONE) && ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilder.append("(select * ,if(operation_type='001', '002', operation_type) as operation_type_temp "
						+ "from ").append(hive_table_name).append(")child1 ");
			}else if((type != Type.PUBLICATION && type != Type.ZONE) && !ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilder.append("(select * , year(dt) as time_year, month(dt) as time_month, day(dt) as time_day, weekofyear(dt) as time_week "
						+ "from ").append(hive_table_name).append(")child1 ");
			}else{
				sqlBuilder.append(hive_table_name);
			}
			if(condition == null){
				sqlBuilder.append(" where 1 ");
			}else{
				sqlBuilder.append(condition);
			}
			
			String groupBy = getGroupByFromList(parameterDto);
			if(type == Type.PUBLICATION || type == Type.ZONE){
				if(parameterDto.getItems().contains("request")){
					sqlBuilder.append(" and operation_type in ('001','002', '003') ");
				}else{
					sqlBuilder.append(" and operation_type = '003' ");
				}
				if(!CommonUtility.isEmptyString(groupBy)){
					sqlBuilder.append(" group by ").append(groupBy);
					sqlBuilder = addFiled(sqlBuilder, "operation_type_temp");
				}else{
					sqlBuilder.append(" group by operation_type_temp");
				}
				if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
					sqlBuilder = addFiled(sqlBuilder, "time_day");
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
				}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_week");
				}
			}else{
				sqlBuilder.append(" and operation_type = '003' ");
				if(!CommonUtility.isEmptyString(groupBy)){
					sqlBuilder.append(" group by ").append(groupBy);
					if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_month");
						sqlBuilder = addFiled(sqlBuilder, "time_day");
					}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_month");
					}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_week");
					}
				}else{
					if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder.append(" group by ");
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_month");
						sqlBuilder = addFiled(sqlBuilder, "time_day");
					}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder.append(" group by ");
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_month");
					}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
						sqlBuilder.append(" group by ");
						sqlBuilder = addFiled(sqlBuilder, "time_year");
						sqlBuilder = addFiled(sqlBuilder, "time_week");
					}
				}
			}
			
		}else if(parameterDto.getFrequency()>10){
			StringBuilder sqlBuilderChild1 = new StringBuilder();
			sqlBuilderChild1.append("select ");
			sqlBuilderChild1.append(getDataFeild(parameterDto, true));
			sqlBuilderChild1.append(", if( count( * ) >10, 11, count( * ) ) as frequency, count( * ) as impression_count ");
			sqlBuilderChild1.append(" from ");
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilderChild1.append("(select * , year(dt) as time_year, month(dt) as time_month, day(dt) as time_day, weekofyear(dt) as time_week "
						+ "from ").append(hive_table_name).append(")child1 ");
			}else{
				sqlBuilderChild1.append(hive_table_name);
			}
//			sqlBuilderChild1.append(" from process_prod.md_device_request_log ");
			String condition = getConditionFromParameter(type,parameterDto);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(condition);
			}
			sqlBuilderChild1.append(" and operation_type = '003' ");
			String groupBy = getGroupByFromList(parameterDto);
			
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilderChild1.append(" group by ").append(groupBy);
				sqlBuilderChild1 = addFiled(sqlBuilderChild1, "equipment_key");
			}else{
				sqlBuilderChild1.append(" group by equipment_key");
			}
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_month");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_day");
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_month");
				}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_week");
				}
			}
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, true));
			sqlBuilder.append(", frequency, count(*) as uv, sum(impression_count) as impression ");
			sqlBuilder.append(" from (");
			sqlBuilder.append(sqlBuilderChild1.toString());
			sqlBuilder.append(" )child2 ");
			sqlBuilder.append(" where frequency > '10' ");
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilder.append(" group by ").append(groupBy);
				sqlBuilder.append(", frequency");
			}else{
				sqlBuilder.append("group by frequency");
			}
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
					sqlBuilder = addFiled(sqlBuilder, "time_day");
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
				}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_week");
				}
			}
		}else{
			StringBuilder sqlBuilderChild1 = new StringBuilder();
			sqlBuilderChild1.append("select ");
			sqlBuilderChild1.append(getDataFeild(parameterDto, true));
			sqlBuilderChild1.append(",count( * ) as frequency ");
//			sqlBuilderChild1.append(" from process_prod.md_device_request_log ");
			sqlBuilderChild1.append(" from ");
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				sqlBuilderChild1.append("(select * , year(dt) as time_year, month(dt) as time_month, day(dt) as time_day, weekofyear(dt) as time_week "
						+ "from ").append(hive_table_name).append(")child1 ");
			}else{
				sqlBuilderChild1.append(hive_table_name);
			}
			String condition = getConditionFromParameter(type,parameterDto);
			if(condition == null){
				sqlBuilderChild1.append(" where 1 ");
			}else{
				sqlBuilderChild1.append(condition);
			}
			sqlBuilderChild1.append(" and operation_type = '003' ");
			String groupBy = getGroupByFromList(parameterDto);
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilderChild1.append(" group by ").append(groupBy);
				sqlBuilderChild1 = addFiled(sqlBuilderChild1, "equipment_key");
			}else{
				sqlBuilderChild1.append(" group by equipment_key");
			}
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_month");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_day");
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_month");
				}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_year");
					sqlBuilderChild1 = addFiled(sqlBuilderChild1, "time_week");
				}
			}
			sqlBuilder.append("select ");
			sqlBuilder.append(getDataFeild(parameterDto, true));
			sqlBuilder.append(", frequency, count(*) as uv, sum(frequency) as impression ");
			sqlBuilder.append(" from (");
			sqlBuilder.append(sqlBuilderChild1.toString());
			sqlBuilder.append(" )child2 ");
			sqlBuilder.append(" where frequency <= '").append(parameterDto.getFrequency()).append("' ");
			if(!CommonUtility.isEmptyString(groupBy)){
				sqlBuilder.append(" group by ").append(groupBy);
				sqlBuilder.append(", frequency");
			}else{
				sqlBuilder.append("group by frequency");
			}
			if(!ParameterDto.DataCycle.TOTAL.toString().equalsIgnoreCase(parameterDto.getDataType())){
				if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
					sqlBuilder = addFiled(sqlBuilder, "time_day");
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_month");
				}else if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					sqlBuilder = addFiled(sqlBuilder, "time_year");
					sqlBuilder = addFiled(sqlBuilder, "time_week");
				}
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
			throw new ReportBaseException(Const.EXCEPTION_BADSQL, "query faile", e);
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
			String time_year = null;
			String time_month = null;
			String time_day = null;
			String time_week = null;
			if(jobResultMap.containsKey("time_year")){
				time_year = String.valueOf(jobResultMap.get("time_year"));
			}
			if(jobResultMap.containsKey("time_month")){
				time_month = String.valueOf(jobResultMap.get("time_month"));
			}
			if(jobResultMap.containsKey("time_day")){
				time_day = String.valueOf(jobResultMap.get("time_day"));
			}
			if(jobResultMap.containsKey("time_week")){
				time_week = String.valueOf(jobResultMap.get("time_week"));
			}
			Calendar calendar = Calendar.getInstance();
			try{
				if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyyww");
					calendar.setMinimalDaysInFirstWeek(4);
					calendar.setTime(format.parse(time_year+time_week));
					calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					jobResultDto.setDate_start(dateFormat.format((parameterDto.getDateRange()[0].getTime()>=calendar.getTime().getTime())? parameterDto.getDateRange()[0] : calendar.getTime()));
					calendar.add(Calendar.DAY_OF_WEEK, 6);
					jobResultDto.setDate_end(dateFormat.format((parameterDto.getDateRange()[1].getTime()<=calendar.getTime().getTime())? parameterDto.getDateRange()[1] : calendar.getTime()));
				}else if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
					jobResultDto.setDate_start(time_year + "-" + time_month + "-" + time_day);
					jobResultDto.setDate_end(time_year + "-" + time_month + "-" + time_day);
				}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
					calendar.setTime(format.parse(time_year + "-" + time_month));
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
		log.debug("return size = \t" + jobResultDtos.size());
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
//		JobResultDto jobresult = null;
//		while(iterator.hasNext()){
//			if(jobresult == null){
//				jobresult = iterator.next();
//			}else{
//				JobResultDto jobresult_1 = iterator.next();
//				if(isSameItemExceptOperationType(jobresult, jobresult_1)){
//					if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
//						jobresult_1.setRequest(jobresult.getImpression());
//						jobresult_1.setUv(jobresult.getUv());
//						jobresult = jobresult_1;
//					}else{
//						jobresult.setRequest(jobresult_1.getImpression());
//					}
//				}else{
//					if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
//						jobresult.setRequest(jobresult.getImpression());
//						jobresult.setUv(0);
//						jobresult.setImpression(0);
//					}
//					jobresults.add(jobresult);
//					jobresult = jobresult_1;
//				}
//			}
//		}
//		if(jobresult!=null){
//			if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
//				jobresult.setRequest(jobresult.getImpression());
//				jobresult.setUv(0);
//				jobresult.setImpression(0);
//			}
//			jobresults.add(jobresult);
//		}
		while(iterator.hasNext()){
			JobResultDto jobresult = iterator.next();
			if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
				jobresult.setRequest(jobresult.impression);
				jobresult.setImpression(0);
				jobresult.setUv(0);
			}else if(Const.OPERATION_TYPE_IMPRESSION.equals(jobresult.getOperation_type())){
				jobresult.setRequest(0);
			}
			if(jobresults.size()==0){
				jobresults.add(jobresult);
			}else{
				boolean hasFlag = false;
				for (int i = 0; i < jobresults.size(); i++) {
					JobResultDto jobresult_1 = jobresults.get(i);
					if(isSameItemExceptOperationType(jobresult, jobresult_1)){
						if(Const.OPERATION_TYPE_REQUST.equals(jobresult.getOperation_type())){
							jobresult_1.setRequest(jobresult.getRequest());
						}else if(Const.OPERATION_TYPE_IMPRESSION.equals(jobresult.getOperation_type())){
							jobresult_1.setImpression(jobresult.getImpression());
							jobresult_1.setUv(jobresult.getUv());
						}
						hasFlag = true;
						break;
					}
				}
				if(!hasFlag){
					jobresults.add(jobresult);
				}
			}
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
				.append("' and ad_date <= '")
				.append(dateFormat.format(parameterDto.getDateRange()[1]))
				.append("' ");
		}
		conditionBuilder.append("and business_id='").append(business_id).append("'");
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
	private String getDataFeild(ParameterDto parameterDto, boolean hasFrequency){
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
						sb_item = addFiled(sb_item, "max("+feild+") as " + feild);
					}
				}
			}
		}
		sb = addFiled(sb, replace(sb_item.toString()));
		if(ParameterDto.DataCycle.BYWEEK.toString().equalsIgnoreCase(parameterDto.getDataType())){
			sb = addFiled(sb, "time_year");
			sb = addFiled(sb, "time_week");
		}else if(ParameterDto.DataCycle.BYDAY.toString().equalsIgnoreCase(parameterDto.getDataType())){
			sb = addFiled(sb, "time_year");
			sb = addFiled(sb, "time_month");
			sb = addFiled(sb, "time_day");
		}else if(ParameterDto.DataCycle.BYMONTH.toString().equalsIgnoreCase(parameterDto.getDataType())){
			sb = addFiled(sb, "time_year");
			sb = addFiled(sb, "time_month");
		}
		if(!hasFrequency){
			if(parameterDto.getType().equals(Type.PUBLICATION.toString()) || parameterDto.getType().equals(Type.ZONE.toString())){
				sb = addFiled(sb, "operation_type_temp");
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
				isRegion_code = true;
			}
		}else{
			if(jobResultDtoB.getRegion_code() == null){
				isRegion_code = true;
			}
		}
		return isIdsEquals && isFrequencyEquals && isDateStartEquals && isDateEndEquals && isRegion_code;
	}
}
