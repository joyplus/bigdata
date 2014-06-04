package tv.joyplus.backend.report.dao.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;


/**
 * 
 * test data : 
 *   
 *   
 *   INSERT INTO  `process`.`md_device_request_log` (`equipment_sn` ,`equipment_key` ,`device_id` ,`device_name` ,`user_pattern` ,`ad_date` ,`datetime` ,`client_time` ,`operation_type` ,`operation_extra` ,`publication_id` ,`zone_id` ,`campaign_id` ,`creative_id` ,`client_ip` ,`screen_type` ,`province_code` ,`city_code` ,`business_id`)
	 VALUES (
	 '',  'e04bb348b65e5c80b15792325c186d76', NULL ,  'E780U',  '',  '2012-01-10',  '2012-01-10 12:00:00', NULL ,  '003',  '',  '48',  '135',  '44',  '10',  '113.90.108.178', NULL ,  'CN_19',  'CN_19_199',  '0'
	 );
	 INSERT INTO  `process`.`md_device_request_log` (`equipment_sn` ,`equipment_key` ,`device_id` ,`device_name` ,`user_pattern` ,`ad_date` ,`datetime` ,`client_time` ,`operation_type` ,`operation_extra` ,`publication_id` ,`zone_id` ,`campaign_id` ,`creative_id` ,`client_ip` ,`screen_type` ,`province_code` ,`city_code` ,`business_id`)
	 VALUES (
	 '',  'e04bb348b65e5c80b15792325c186d76', NULL ,  'E780U',  '',  '2012-01-10',  '2012-01-10 12:00:00', NULL ,  '002',  '',  '48',  '135',  '44',  '10',  '113.90.108.178', NULL ,  'CN_19',  'CN_19_199',  '0'
	 );
 * 
 * 
 * @author qibozhang
 *
 */

@ContextConfiguration(locations = {"classpath:ApplicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessDaoImplTest {
	
	@Autowired
	private ProcessDao processDao;
	
	@Test
	public void testQueryDataByCampaign() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"44"};
		String [] data_items = {"uv","impression"};
		String [] data_items_1 = {"uv","impression","campaign_owner"};
		String [] groupby_items = {"zone_name"};
		String [] groupby_items_1 = {"zone_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","inv_name","zone_name","device_name","province"};
		String [] groupby_items_3 = {"campaign_name","inv_name","zone_name","device_name","city"};
		
		// test frequency = -1  total
		List<JobResultDto> list = queryData("campaign", "total", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1  byDay
		List<JobResultDto> list_byDay = queryData("campaign", "byDay", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("campaign byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("campaign byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("campaign byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("campaign byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("campaign byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
			
		// test frequency = -1  byWeek
		List<JobResultDto> list_byWeek = queryData("campaign", "byWeek", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("campaign byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byWeek.size());
		assertEquals("campaign byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byWeek.get(0).uv);
		assertEquals("campaign byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byWeek.get(0).impression);
		assertEquals("campaign byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-08",list_byWeek.get(0).date_start);
		assertEquals("campaign byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-14",list_byWeek.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("campaign", "byMonth", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("campaign byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("campaign byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , campain_ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("campaign byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("campaign byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("campaign byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);
		
		// test frequency = -1  total group by groupby_items_2[] (province)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("campaign", "total", -1, ids, dateRange, data_items_1, groupby_items_2);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error",135,list_total_data_itmes_1_groupby_items_2.get(0).zone_id);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		
		// test frequency = -1  total group by groupby_items_3[] (city_code)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("campaign", "total", -1, ids, dateRange, data_items_1, groupby_items_3);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error",135,list_total_data_itmes_1_groupby_items_3.get(0).zone_id);
		assertEquals("campaign total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);

		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("campaign", "total", 10, ids, dateRange, data_items, groupby_items_1);
		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		List<JobResultDto> list_frequency_11 = queryData("campaign", "total", 11, ids, dateRange, data_items, groupby_items_1);
		assertEquals("campaign total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("campaign total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
	}
	
	@Test
	public void testQueryDataByUnit() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] unit_ids = {"10"};
		String [] data_itmes = {"uv","impression"};
		String [] data_itmes_1 = {"uv","impression","adv_size","adv_type","adv_ext"};
		String [] groupby_items = {"inv_name"};
		String [] groupby_items_1 = {"inv_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","inv_name","zone_name","device_name","province"};
		String [] groupby_items_3 = {"campaign_name","inv_name","zone_name","device_name","city"};
		
		// test frequency = -1 total
		List<JobResultDto> list = queryData("unit", "total", -1, unit_ids, dateRange, data_itmes, groupby_items);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1 byDay
		List<JobResultDto> list_byDay = queryData("unit", "byDay", -1, unit_ids, dateRange, data_itmes, groupby_items);
		assertEquals("unit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("unit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("unit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("unit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("unit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
		
		// test frequency = -1  byWeek
		List<JobResultDto> list_byWeek = queryData("unit", "byWeek", -1, unit_ids, dateRange, data_itmes, groupby_items);
		assertEquals("unit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byWeek.size());
		assertEquals("unit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byWeek.get(0).uv);
		assertEquals("unit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byWeek.get(0).impression);
		assertEquals("unit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-08",list_byWeek.get(0).date_start);
		assertEquals("unit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-14",list_byWeek.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("unit", "byMonth", -1, unit_ids, dateRange, data_itmes, groupby_items);
		assertEquals("unit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("unit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("unit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("unit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("unit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);
	
		// test frequency = -1  total group by groupby_items_2[] (province_code)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("unit", "total", -1, unit_ids, dateRange, data_itmes_1, groupby_items_2);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",10,list_total_data_itmes_1_groupby_items_2.get(0).adv_id);
		
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error",135,list_total_data_itmes_1_groupby_items_2.get(0).zone_id);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		
		// test frequency = -1  total group by groupby_items_3[] (city_code)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("unit", "total", -1, unit_ids, dateRange, data_itmes_1, groupby_items_3);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",10,list_total_data_itmes_1_groupby_items_3.get(0).adv_id);
		
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error",135,list_total_data_itmes_1_groupby_items_3.get(0).zone_id);
		assertEquals("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);
		
		if(queryData("unit", "total", -1, unit_ids, dateRange, data_itmes_1, groupby_items_3) == null){
			fail("unit total frequency = -1 start 2012-01-01 end 2012-05-31,"
					+ " , unit_ids " + array2String(unit_ids)
					+ " , data_itmes " + array2String(data_itmes_1)
					+ " , groupby_items " + array2String(groupby_items_3)
					+ " query Faile");
		}
		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("unit", "total", 10, unit_ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		List<JobResultDto> list_frequency_11 = queryData("unit", "total", 11, unit_ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("unit total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , unit_ids " + array2String(unit_ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , unit_ids " + Array2String(unit_ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , unit_ids " + Array2String(unit_ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("unit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , unit_ids " + Array2String(unit_ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
	}
	
	@Test
	public void testQueryDataByPublication() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"48"};
		String [] data_itmes = {"uv","impression"};
		String [] data_itmes_1 = {"uv","impression","request"};
		String [] groupby_items = {"inv_name"};
		String [] groupby_items_1 = {"inv_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","inv_name","adv_name","device_name","province"};
		String [] groupby_items_3 = {"campaign_name","inv_name","adv_name","device_name","city"};
		
		
		// test frequency = -1 total
		List<JobResultDto> list = queryData("publication", "total", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1 byDay
		List<JobResultDto> list_byDay = queryData("publication", "byDay", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("publication byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("publication byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("publication byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("publication byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("publication byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("publication", "byMonth", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("publication byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("publication byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("publication byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("publication byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("publication byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);

		// test frequency = -1  total group by groupby_items_2[] (province_code) request
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("publication", "total", -1, ids, dateRange, data_itmes_1, groupby_items_2);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile request error",1,list_total_data_itmes_1_groupby_items_2.get(0).request);
		
		// test frequency = -1  total group by groupby_items_2[] (province_code) request
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("publication", "total", -1, ids, dateRange, data_itmes_1, groupby_items_3);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);
		assertEquals("publication total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile request error",1,list_total_data_itmes_1_groupby_items_3.get(0).request);
			
		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("publication", "total", 10, ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		// test  frequency >10
		List<JobResultDto> list_frequency_11 = queryData("publication", "total", 11, ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("publication total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("publication total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
	}
	
	@Test
	public void testQueryDataByZone() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"135"};
		String [] data_itmes = {"uv","impression"};
		String [] data_itmes_1 = {"uv","impression","request","zone_type","zone_size"};
		String [] groupby_items = {"inv_name"};
		String [] groupby_items_1 = {"inv_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","inv_name","zone_name","device_name","province"};
		String [] groupby_items_3 = {"campaign_name","inv_name","zone_name","device_name","city"};
	
		
		// test frequency = -1 total
		List<JobResultDto> list = queryData("zone", "total", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1 byDay
		List<JobResultDto> list_byDay = queryData("zone", "byDay", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("zone byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("zone byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("zone byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("zone byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("zone byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("zone", "byMonth", -1, ids, dateRange, data_itmes, groupby_items);
		assertEquals("zone byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("zone byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("zone byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("zone byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , zone " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("zone byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);

		// test frequency = -1  total group by groupby_items_2[] (province_code) request
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("zone", "total", -1, ids, dateRange, data_itmes_1, groupby_items_2);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile request error",1,list_total_data_itmes_1_groupby_items_2.get(0).request);
		
		// test frequency = -1  total group by groupby_items_2[] (province_code) request
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("zone", "total", -1, ids, dateRange, data_itmes_1, groupby_items_3);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);
		assertEquals("zone total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile request error",1,list_total_data_itmes_1_groupby_items_3.get(0).request);
			
		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("zone", "total", 10, ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		// test  frequency >10
		List<JobResultDto> list_frequency_11 = queryData("zone", "total", 11, ids, dateRange, data_itmes, groupby_items_1);
		assertEquals("zone total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_itmes)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("zone total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + Array2String(ids)
//				+ " , data_itmes " + Array2String(data_itmes)
//				+ " , groupby_items " + Array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);

	}
	
	@Test
	public void testQueryDataByMonitor() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"44"};
		String [] data_items = {"uv","impression"};
		String [] groupby_items = {"campaign_name"};
		String [] groupby_items_1 = {"campaign_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","device_name","province"};
		String [] groupby_items_3 = {"campaign_name","device_name","city"};
		
		// test frequency = -1  total
		List<JobResultDto> list = queryData("monitor", "total", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1  byDay
		List<JobResultDto> list_byDay = queryData("monitor", "byDay", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitor byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("monitor byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("monitor byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("monitor byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("monitor byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
			
		// test frequency = -1  byWeek
		List<JobResultDto> list_byWeek = queryData("monitor", "byWeek", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitor byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byWeek.size());
		assertEquals("monitor byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byWeek.get(0).uv);
		assertEquals("monitor byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byWeek.get(0).impression);
		assertEquals("monitor byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-08",list_byWeek.get(0).date_start);
		assertEquals("monitor byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-14",list_byWeek.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("monitor", "byMonth", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitor byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("monitor byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , campain_ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("monitor byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("monitor byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("monitor byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);
		
		// test frequency = -1  total group by groupby_items_2[] (province)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("monitor", "total", -1, ids, dateRange, data_items, groupby_items_2);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","E780U",list_total_data_itmes_1_groupby_items_2.get(0).device_name);
		
		// test frequency = -1  total group by groupby_items_3[] (city_code)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("monitor", "total", -1, ids, dateRange, data_items, groupby_items_3);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);
		assertEquals("monitor total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","E780U",list_total_data_itmes_1_groupby_items_3.get(0).device_name);

		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("monitor", "total", 10, ids, dateRange, data_items, groupby_items_1);
		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		List<JobResultDto> list_frequency_11 = queryData("monitor", "total", 11, ids, dateRange, data_items, groupby_items_1);
		assertEquals("monitor total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("monitor total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
	}
	
	
	@Test
	public void testQueryDataByMonitorUnit() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"10"};
		String [] data_items = {"uv","impression"};
		String [] data_items_1 = {"uv","impression","campaign_name"};
		String [] groupby_items = {"adv_name"};
		String [] groupby_items_1 = {"adv_name","frequency"};
		String [] groupby_items_2 = {"inv_name","device_name","province"};
		String [] groupby_items_3 = {"inv_name","device_name","city"};
		
		// test frequency = -1
		List<JobResultDto> list = queryData("monitorUnit", "total", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1  byDay
		List<JobResultDto> list_byDay = queryData("monitorUnit", "byDay", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitorUnit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byDay.size());
		assertEquals("monitorUnit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byDay.get(0).uv);
		assertEquals("monitorUnit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byDay.get(0).impression);
		assertEquals("monitorUnit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("monitorUnit byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
			
		// test frequency = -1  byWeek
		List<JobResultDto> list_byWeek = queryData("monitorUnit", "byWeek", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitorUnit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byWeek.size());
		assertEquals("monitorUnit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byWeek.get(0).uv);
		assertEquals("monitorUnit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byWeek.get(0).impression);
		assertEquals("monitorUnit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-08",list_byWeek.get(0).date_start);
		assertEquals("monitorUnit byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-14",list_byWeek.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("monitorUnit", "byMonth", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("monitorUnit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list_byMonth.size());
		assertEquals("monitorUnit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , campain_ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list_byMonth.get(0).uv);
		assertEquals("monitorUnit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list_byMonth.get(0).impression);
		assertEquals("monitorUnit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("monitorUnit byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);
		
		// test frequency = -1  total group by groupby_items_2[] (province)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("monitorUnit", "total", -1, ids, dateRange, data_items_1, groupby_items_2);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile frequency error","CN_19",list_total_data_itmes_1_groupby_items_2.get(0).region_code);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile device name error","E780U",list_total_data_itmes_1_groupby_items_2.get(0).device_name);
		
		// test frequency = -1  total group by groupby_items_3[] (city_code)
		List<JobResultDto> list_total_data_itmes_1_groupby_items_3 = queryData("monitorUnit", "total", -1, ids, dateRange, data_items_1, groupby_items_3);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_3.size());
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_3.get(0).uv);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_3.get(0).impression);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile frequency error","CN_19_199",list_total_data_itmes_1_groupby_items_3.get(0).region_code);
		assertEquals("monitorUnit total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items_1)
				+ " , groupby_items " + array2String(groupby_items_3)
				+ " query Faile device name error","E780U",list_total_data_itmes_1_groupby_items_3.get(0).device_name);

		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("monitorUnit", "total", 10, ids, dateRange, data_items, groupby_items_1);
		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		List<JobResultDto> list_frequency_11 = queryData("monitorUnit", "total", 11, ids, dateRange, data_items, groupby_items_1);
		assertEquals("monitorUnit total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("monitorUnit total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
	}
	
	
	@Test
	public void testQueryDataByLocation() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = sdf.parse("2012-01-01");
		Date endTime = sdf.parse("2012-05-31");
		Date[] dateRange = {startTime,endTime};
		
		
		String [] ids = {"CN_19_199"};
		String [] data_items = {"uv","impression"};
		String [] groupby_items = {"campaign_name"};
		String [] groupby_items_1 = {"campaign_name","frequency"};
		String [] groupby_items_2 = {"campaign_name","adv_name","inv_name","zone_name","device_name"};
		
		List<JobResultDto> list = queryData("location", "total", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		
		// test frequency = -1  byDay
		List<JobResultDto> list_byDay = queryData("location", "byDay", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		assertEquals("location byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-10",list_byDay.get(0).date_start);
		assertEquals("location byDay frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-10",list_byDay.get(0).date_end);
			
		// test frequency = -1  byWeek
		List<JobResultDto> list_byWeek = queryData("location", "byWeek", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		assertEquals("location byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-08",list_byWeek.get(0).date_start);
		assertEquals("location byWeek frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-14",list_byWeek.get(0).date_end);
		
		// test frequency = -1  byMonth
		List<JobResultDto> list_byMonth = queryData("location", "byMonth", -1, ids, dateRange, data_items, groupby_items);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile size error",1,list.size());
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile uv error",1,list.get(0).uv);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile impression error",1,list.get(0).impression);
		assertEquals("location byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile date_start error","2012-01-01",list_byMonth.get(0).date_start);
		assertEquals("location byMonth frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items)
				+ " query Faile byDay date_end error","2012-01-31",list_byMonth.get(0).date_end);
		
		// test frequency = -1  total group by groupby_items_2 ("campaign_name","adv_name","inv_name","zone_name")
		List<JobResultDto> list_total_data_itmes_1_groupby_items_2 = queryData("location", "total", -1, ids, dateRange, data_items, groupby_items_2);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile size error",1,list_total_data_itmes_1_groupby_items_2.size());
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile uv error",1,list_total_data_itmes_1_groupby_items_2.get(0).uv);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile impression error",1,list_total_data_itmes_1_groupby_items_2.get(0).impression);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile campaign_id error",44,list_total_data_itmes_1_groupby_items_2.get(0).campaign_id);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile adv_id error",10,list_total_data_itmes_1_groupby_items_2.get(0).adv_id);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile zone_id error",135,list_total_data_itmes_1_groupby_items_2.get(0).zone_id);
		assertEquals("location total frequency = -1 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_2)
				+ " query Faile device name error","E780U",list_total_data_itmes_1_groupby_items_2.get(0).device_name);


		
		// test   10>=frequency >0
		List<JobResultDto> list_frequency = queryData("location", "total", 10, ids, dateRange, data_items, groupby_items_1);
		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",1,list_frequency.size());
		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile uv error",1,list_frequency.get(0).uv);
		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile impression error",1,list_frequency.get(0).impression);
		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile frequency error",1,list_frequency.get(0).frequency);
		
		List<JobResultDto> list_frequency_11 = queryData("location", "total", 11, ids, dateRange, data_items, groupby_items_1);
		assertEquals("location total frequency = 11 start 2012-01-01 end 2012-05-31,"
				+ " , ids " + array2String(ids)
				+ " , data_itmes " + array2String(data_items)
				+ " , groupby_items " + array2String(groupby_items_1)
				+ " query Faile size error",0,list_frequency_11.size());
//		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile uv error",1,list_frequency_11.get(0).uv);
//		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile impression error",1,list_frequency_11.get(0).impression);
//		assertEquals("location total frequency = 10 start 2012-01-01 end 2012-05-31,"
//				+ " , ids " + array2String(ids)
//				+ " , data_itmes " + array2String(data_items)
//				+ " , groupby_items " + array2String(groupby_items_1)
//				+ " query Faile frequency error",1,list_frequency_11.get(0).frequency);
		
	}
	
	
	private List<JobResultDto> queryData(String type, String datetype, int frequency, 
			String[] dataResource, Date[] dateRange, String[] items, String[] groupby){
		
		ParameterDto parameterDto = new ParameterDto();
		parameterDto.setReportId(String.valueOf("1"));
		parameterDto.setType(type);
		parameterDto.setDataType(datetype);
		parameterDto.setFrequency(frequency);
		List<String> dataResourcesArrayList = new ArrayList<String>();
		for(String dr : dataResource){
			dataResourcesArrayList.add(dr);
		}
		parameterDto.setDataResource(dataResourcesArrayList);
		
		parameterDto.setDateRange(dateRange);
		
		List<String> itemsArrayList = new ArrayList<String>();
		for(String item : items){
			itemsArrayList.add(item);
		}
		parameterDto.setItems(itemsArrayList);
		
		List<String> groupbyArrayList = new ArrayList<String>();
		for(String gb : groupby){
			groupbyArrayList.add(gb);
		}
		parameterDto.setGroupby(groupbyArrayList);
		
		return processDao.queryData(parameterDto);
	}
	
	
	private String array2String(String[] array){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			if(i!=0){
				sb.append(",");
			}
			sb.append(array[i]);
		}
		sb.append("]");
		return sb.toString();
	}
}
