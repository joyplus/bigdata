package tv.joyplus.backend.report.dto;

import java.util.Date;

import com.sun.tools.javac.util.List;

public class ParameterDto {
	
	private String reportId;
	private String dataType;
	private List<String> dataResource;
	private Date[] dateRange = new Date[2];
	
    public enum dataCycle {
    	total, byDay, byWeek, byMonth;
    }
	private int frequency;
	private List<String> groupby;
	private List<String> items;
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public List<String> getDataResource() {
		return dataResource;
	}
	public void setDataResource(List<String> dataResource) {
		this.dataResource = dataResource;
	}
	public Date[] getDateRange() {
		return dateRange;
	}
	public void setDateRange(Date[] dateRange) {
		this.dateRange = dateRange;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public List<String> getGroupby() {
		return groupby;
	}
	public void setGroupby(List<String> groupby) {
		this.groupby = groupby;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	
	
	

//	
//	{
//		"type":"campaign",
//		"dataResource":["68","72","73","74"],
//		"dateRange":["2014-05-04","2014-05-10"],
//		"dataCycle":"total",
//		"frequency":"1",
//		"groupBy":["hour","campaign_name","device_brands","device_name","frequency","province"],
//		"items":["campaign_id","campaign_owner","inv_name","zone_name","impression","uv”]
//		}
//		张云娥  17:33:44
//		// 报表类型
//		type:  string   
//		campaign , unit , publication, zone, monitor, monitorUnit
//
//		//  资源id ( 为campaign_id或者 unit_id,  由type值决定 )
//		dataResource: Array   ( 例如： [ 56, 80, 81] )
//
//		//  起至时间
//		dateRange: Array  ( [2014-05-01, 2014-05-10] )
//
//		// 分日，分周，分月
//		dataCycle: string
//		total ( 汇总 ), byDay, byWeek, byMonth
//
//		// 频次塞选条件
//		frequency:  int
//		frequency ＝ -1   不按频次分组
//		frequency ＝ 1-10:  表示频次小于等于frequency
//		frequency ＝11 表示频次大于10
//
//		// 分组
//		groupBy: Array
//		hour,campaign_name,quality,device_brands,device_name,device_movement, frequency, province,city
//		hour,adv_name,quality,device_brands,device_name,device_movement, frequency,province,city
//		hour,inv_name,quality,device_brands,device_name,device_movement, frequency,province,city
//		hour,zone_name,quality,device_brands,device_name,device_movement, frequency,province,city
//		hour,campaign_name,device_movement, frequency,province,city
//		hour,adv_name,device_movement, frequency,province,city
//
//		// 数据项目  包含分组的数据列 和日期（分日，分周，分月）
//		items: Array
//		campaign_id,campaign_owner,inv_name,zone_name,impression,uv
//		campaign_name,campaign_owner,adv_id,adv_size,adv_type,adv_ext,inv_name,zone_name,impression,uv
//		inv_id,campaign_name,adv_name,impression,uv
//		inv_id,zone_type,zone_size,campaign_name,adv_name,inv_name,impression,uv
//		campaign_id,impression,uv
//		campaign_id,campaign_name,impression,uv
}
