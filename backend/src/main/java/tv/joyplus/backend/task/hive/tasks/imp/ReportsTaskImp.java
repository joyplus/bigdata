package tv.joyplus.backend.task.hive.tasks.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;

import tv.joyplus.backend.task.hive.hibernate.util.HibernateUtil;
import tv.joyplus.backend.task.hive.model.Reports;
import tv.joyplus.backend.task.hive.tasks.ReportsTask;

public class ReportsTaskImp extends ReportsTask{

	private JdbcTemplate hiveJdbcTemplate;
//	private JdbcTemplate mysqlJdbcTemplate;
	private String hiveTableName;
	private String businessId;
	protected Log log = LogFactory.getLog(ReportsTaskImp.class);
	@Override
	public List<Reports> getReports(String date) {
		// TODO Auto-generated method stub
		
		String childSql = "select * ,if( operation_type='001', '002', operation_type ) as operation_type_temp "
	            + " from " + hiveTableName + " where ad_date = '"+ date +"' and business_id = '" + businessId 
	            + "'  and operation_type in ('001','002','003','005')";
		String sql = "select campaign_id  ,  creative_id  ,  publication_id  ,zone_id,  device_name,"
				+ " province_code, city_code  ,   count(*) as impression,  count(distinct equipment_key) as uv ,operation_type_temp"
	            + " from (" + childSql + ")child group by campaign_id,  creative_id,  publication_id, zone_id, device_name,  province_code, city_code,  operation_type_temp";

		log.info("Running sql-->" + sql);
		List<Map<String, Object>> result = hiveJdbcTemplate.queryForList(sql);
		
		Iterator<Map<String,Object>> it = result.iterator();
		List<Reports> reports = new ArrayList<Reports>();
		Session session = HibernateUtil.getSeesion();
		while (it.hasNext()) {
			Map<String, Object> map = it.next();
			Reports report = new Reports();
			report.setCampaign_id(Integer.valueOf(String.valueOf(map.get("campaign_id"))));
			report.setAd_id(Integer.valueOf(String.valueOf(map.get("creative_id"))));
			report.setPublication_id(Integer.valueOf(String.valueOf(map.get("publication_id"))));
			report.setZone_id(Integer.valueOf(String.valueOf(map.get("zone_id"))));
			String device_name = String.valueOf(map.get("device_name"));
			Query query = session.createSQLQuery("select device_id from md_devices where "
					+ "(device_name='"+device_name+"' OR  device_movement='"+device_name+"') and del_flg!=1");
			int device_id = 0;
			List<Object> result_device_list = query.list();
			if(result_device_list  !=null &&result_device_list.size()>0){
				device_id = Integer.valueOf(String.valueOf(result_device_list.get(0)));
			}
//			if(query.uniqueResult()!=null && !"null".equals(query.uniqueResult())){
//				device_id = Integer.valueOf(String.valueOf(query.uniqueResult()));
//			}
			report.setDevice_id(device_id);
			report.setDate(date);
			report.setProvince_code(String.valueOf(map.get("province_code")));
			report.setCity_code(String.valueOf(map.get("city_code")));
			int type = Integer.valueOf(String.valueOf(map.get("operation_type_temp")));
			int impression = Integer.valueOf(String.valueOf(map.get("impression")));
			int uv = Integer.valueOf(String.valueOf(map.get("uv")));
			report.setType(type);
			switch (type) {
			case 3:
				report.setImpression(impression);
				report.setUv(uv);
				report.setRequest(0);
				report.setClicks(0);
				break;
			case 2:
				report.setImpression(0);
				report.setUv(0);
				report.setRequest(impression);
				report.setClicks(0);
				break;
			case 5:
				report.setImpression(0);
				report.setUv(0);
				report.setRequest(0);
				report.setClicks(impression);
				break;

			default:
				return null;
			}
			reports.add(report);
		}
		session.close();
		return reports;
	}

	@Override
	public void saveReports(List<Reports> reports) {
		// TODO Auto-generated method stub
		if(reports==null){
			return;
		}
		Session session = HibernateUtil.getSeesion();
		session.beginTransaction();
		Iterator<Reports> its = reports.iterator();
		while (its.hasNext()) {
			Reports report = its.next();
			Query query = session.createQuery("from Reports where date=:date AND campaign_id=:campaign_id AND ad_id=:ad_id"
					+ " AND publication_id=:publication_id AND zone_id=:zone_id AND province_code=:province_code"
					+ " AND city_code=:city_code AND device_id=:device_id");
			query.setParameter("date", report.getDate())
				.setParameter("campaign_id", report.getCampaign_id())
				.setParameter("ad_id", report.getAd_id())
				.setParameter("publication_id", report.getPublication_id())
				.setParameter("zone_id", report.getZone_id())
				.setParameter("province_code", report.getProvince_code())
				.setParameter("city_code", report.getCity_code())
				.setParameter("device_id", report.getDevice_id());
			Reports report_saved = (Reports)query.uniqueResult();
			if(report_saved==null){
				session.save(report);
			}else{
				switch (report.getType()) {
				case 3:
					report_saved.setImpression(report.getImpression());
					report_saved.setUv(report.getUv());
					break;
				case 2:
					report_saved.setRequest(report.getRequest());
					break;
				case 5:
					report_saved.setClicks(report.getClicks());
					break;
				default:
					break;
				}
				session.update(report_saved);
			}
		}
		session.getTransaction().commit();
		session.close();
	}
	

	public JdbcTemplate getHiveJdbcTemplate() {
		return hiveJdbcTemplate;
	}

	public void setHiveJdbcTemplate(JdbcTemplate hiveJdbcTemplate) {
		this.hiveJdbcTemplate = hiveJdbcTemplate;
	}

//	public JdbcTemplate getMysqlJdbcTemplate() {
//		return mysqlJdbcTemplate;
//	}
//
//	public void setMysqlJdbcTemplate(JdbcTemplate mysqlJdbcTemplate) {
//		this.mysqlJdbcTemplate = mysqlJdbcTemplate;
//	}

	public String getHiveTableName() {
		return hiveTableName;
	}

	public void setHiveTableName(String hiveTableName) {
		this.hiveTableName = hiveTableName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}
