package tv.joyplus.backend.task.hive.tasks.imp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
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
import tv.joyplus.backend.task.hive.model.FrequencyReport;
import tv.joyplus.backend.task.hive.tasks.FrequencyTask;


public class FrequencyTaskImp extends FrequencyTask {

	private JdbcTemplate hiveJdbcTemplate;
	private String hiveTableName;
	private String businessId;
	protected Log log = LogFactory.getLog(FrequencyTaskImp.class);
	
	@Override
	public String getAdIdSet(String date) {
		// TODO Auto-generated method stub
		String sql="select creative_id from " + hiveTableName +" where business_id='"
				+ businessId+"' and ad_date='"+date+"' group by creative_id";
		log.info("FrequencyTask Running-->"+sql);
		List<Map<String, Object>> result = hiveJdbcTemplate.queryForList(sql);
		Iterator<Map<String,Object>> it = result.iterator();
		String ads=null;
		while (it.hasNext()) {
			Map<String, Object> map = (Map<String, Object>)it.next();
			String ad_id = String.valueOf(map.get("creative_id"));
			if(ads==null){
				ads = ad_id;
			}else{
				if(Integer.valueOf(ad_id)!=0){
					ads += ("," + ad_id);
				}
			}
		}
		return ads;
	}

	@Override
	public List<FrequencyReport> getReports(String ads, String date) {
		// TODO Auto-generated method stub
		String sql="SELECT campaign_id, creative_id,publication_id,zone_id, rowcount, count(*) as frequency"
				+ " from (select if(count(*)>=30, 30, count(*))as rowcount,equipment_key,campaign_id,creative_id,publication_id,zone_id "
						+ " FROM "+hiveTableName+" WHERE operation_type='003' and business_id='"+businessId+"' "
						+ " and ad_date>='2014-01-08' and ad_date<='"+date+"' and creative_id in ("+ ads +") "
						+ " GROUP BY campaign_id, creative_id,publication_id,zone_id, equipment_key)d "
				+ " GROUP BY campaign_id, creative_id,publication_id,zone_id, rowcount";
		log.info("FrequencyTask Running-->"+sql);
		try{
			List<FrequencyReport> reports = new ArrayList<FrequencyReport>();
//			List<Map<String, Object>> result = hiveJdbcTemplate.queryForList(sql);
//			Iterator<Map<String,Object>> it = result.iterator();
//			while(it.hasNext()){
//				Map<String, Object> map = it.next();
//				FrequencyReport report = new FrequencyReport();
//				if(map.get("campaign_id")!=null && !"null".equalsIgnoreCase(String.valueOf(map.get("campaign_id")))){
//					report.setCampaign_id(Integer.valueOf(String.valueOf(map.get("campaign_id"))));
//				}
//				if(map.get("creative_id")!=null && !"null".equalsIgnoreCase(String.valueOf(map.get("creative_id")))){
//					report.setAd_id(Integer.valueOf(String.valueOf(map.get("creative_id"))));
//				}
//				if(map.get("publication_id")!=null && !"null".equalsIgnoreCase(String.valueOf(map.get("publication_id")))){
//					report.setPublication_id(Integer.valueOf(String.valueOf(map.get("publication_id"))));
//				}
//				if(map.get("zone_id")!=null && !"null".equalsIgnoreCase(String.valueOf(map.get("zone_id")))){
//					report.setZone_id(Integer.valueOf(String.valueOf(map.get("zone_id"))));
//				}
//				report.setDate(date);
//				int frequency = Integer.valueOf(String.valueOf(map.get("rowcount")));
//				int number = Integer.valueOf(String.valueOf(map.get("frequency")));
//				boolean already_in_list = false;
//				for(FrequencyReport report_inlist : reports){
//					if(isSameReports(report_inlist, report)){
//						setReportFrequency(report_inlist, frequency, number);
//						already_in_list = true;
//						break;
//					}
//				}
//				if(!already_in_list){
//					setReportFrequency(report,frequency,number);
//					reports.add(report);
//				}
//			}
			Statement stmt = hiveJdbcTemplate.getDataSource().getConnection().createStatement();
			stmt.execute("set mapred.child.java.opts=-Xmx1024m");
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				FrequencyReport report = new FrequencyReport();
				if(result.getString(1)!=null){
					report.setCampaign_id(Integer.valueOf(result.getString(1)));
				}
				if(result.getString(2)!=null){
					report.setAd_id(Integer.valueOf(result.getString(2)));
				}
				if(result.getString(3)!=null){
					report.setPublication_id(Integer.valueOf(result.getString(3)));
				}
				if(result.getString(4)!=null){
					report.setZone_id(Integer.valueOf(result.getString(4)));
				}
				report.setDate(date);
				int frequency = Integer.valueOf(result.getString(5));
				int number = Integer.valueOf(result.getString(6));
				boolean already_in_list = false;
				for(FrequencyReport report_inlist : reports){
					if(isSameReports(report_inlist, report)){
						setReportFrequency(report_inlist, frequency, number);
						already_in_list = true;
						break;
					}
				}
				if(!already_in_list){
					setReportFrequency(report,frequency,number);
					reports.add(report);
				}
			}
			result.close();
			stmt.close();
			return reports;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("FrequencyTask error",e);
			return null;
		}
	}

	@Override
	public void saveReports(List<FrequencyReport> reports){
		// TODO Auto-generated method stub
		if(reports==null){
			return;
		}
		Session session = null;
		try{
			session = HibernateUtil.getSeesion();
			session.beginTransaction();
			Iterator<FrequencyReport> its = reports.iterator();
			while (its.hasNext()) {
				FrequencyReport report = its.next();
				String hql = "from FrequencyReport where campaign_id=:campaign_id and ad_id=:ad_id"
						+ " and publication_id=:publication_id and zone_id=:zone_id and date=:date";
				Query query = session.createQuery(hql);
				query.setParameter("campaign_id", report.getCampaign_id());
				query.setParameter("ad_id", report.getAd_id());
				query.setParameter("publication_id", report.getPublication_id());
				query.setParameter("zone_id", report.getZone_id());
				query.setParameter("date", report.getDate());
				FrequencyReport report_saved = (FrequencyReport) query.uniqueResult();
				if(report_saved==null){
					session.save(report);
				}else{
					for(int i=1; i<=30; i++){
						setReportFrequency(report_saved, i, getReportFrequencyNumber(report, i));
					}
					session.update(report_saved);
				}
				session.flush();
			}
			session.getTransaction().commit();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("FrequencyTask error",e);
		}finally{
			if(session!=null){
				session.close();
			}
		}
	}
	
	private void setReportFrequency(FrequencyReport report, int frequency, int number) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		String method_name = "setN"+frequency;
		Class[] parameterTypes = new Class[1];
		Field field = report.getClass().getDeclaredField("n"+frequency);
		parameterTypes[0] = field.getType();
		Method method = report.getClass().getMethod(method_name, parameterTypes);
		method.invoke(report, new Object[] { number });
	}
	
	private int getReportFrequencyNumber(FrequencyReport report, int frequency) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		String method_name = "getN"+frequency;
		Method method = report.getClass().getMethod(method_name);
		int number =  (Integer) method.invoke(report); 
		return number;
	}

	private boolean isSameReports(FrequencyReport a, FrequencyReport b){
		int c_a = a.getCampaign_id();
		int a_a = a.getAd_id();
		int p_a = a.getPublication_id();
		int z_a = a.getZone_id();
		String d_a = a.getDate();
		
		int c_b = b.getCampaign_id();
		int a_b = b.getAd_id();
		int p_b = b.getPublication_id();
		int z_b = b.getZone_id();
		String d_b = b.getDate();
		
		boolean result = c_a==c_b && a_a==a_b && p_a==p_b && z_a==z_b && d_a.equals(d_b);
		return result;
	}
	
	public JdbcTemplate getHiveJdbcTemplate() {
		return hiveJdbcTemplate;
	}

	public void setHiveJdbcTemplate(JdbcTemplate hiveJdbcTemplate) {
		this.hiveJdbcTemplate = hiveJdbcTemplate;
	}

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
