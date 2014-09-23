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
import tv.joyplus.backend.task.hive.model.Campaign;
import tv.joyplus.backend.task.hive.model.Publication;
import tv.joyplus.backend.task.hive.model.PublicationReports;
import tv.joyplus.backend.task.hive.model.Zone;
import tv.joyplus.backend.task.hive.tasks.PublicationReportsTask;

public class PublicationReportsTaskImp extends PublicationReportsTask {

	private JdbcTemplate hiveJdbcTemplate;
	private String hiveTableName;
	private String businessId;
	protected Log log = LogFactory.getLog(PublicationReportsTaskImp.class);
	
	@Override
	public List<PublicationReports> getPublicationReports(String date) {
		// TODO Auto-generated method stub
		String sql = "select zone_id, count(*) as impression,  count(distinct equipment_key) as uv "
	            +" from "+ hiveTableName +" where ad_date = '"+ date +"' and business_id = '"+ businessId+"' and operation_type = '003'"
	            +" group by zone_id";
		return getReports(date, sql);
	}

	@Override
	public List<PublicationReports> getPublicationReportsWithCampaign(
			String date) {
		// TODO Auto-generated method stub
		String sql = "select campaign_id,zone_id, count(*) as impression,  count(distinct equipment_key) as uv "
				+" from "+ hiveTableName +" where ad_date = '"+ date +"' and business_id = '"+ businessId+"' and operation_type = '003'"
	            +" group by campaign_id, zone_id";
		return getReports(date, sql);
	}

	@Override
	public void saveReports(List<PublicationReports> reports) {
		// TODO Auto-generated method stub
		try{
			Session session = HibernateUtil.getSeesion();
			session.beginTransaction();
			Iterator<PublicationReports> its = reports.iterator();
			while (its.hasNext()) {
				PublicationReports report = its.next();
				String hql = "from PublicationReports where zone_id=:zone_id and date=:date";
				if(report.getCampaign_id()!=null){
					hql += (" and campaign_id =" + report.getCampaign_id());
				}else{
					hql += " and campaign_id is NULL";
				}
				Query query = session.createQuery(hql);
				query.setParameter("zone_id", report.getZone_id());
				query.setParameter("date", report.getDate());
				PublicationReports report_saved = (PublicationReports) query.uniqueResult();
				if(report_saved==null){
					session.save(report);
				}else{
					report_saved.setPublication_id(report.getPublication_id());
					report_saved.setPublication_name(report.getPublication_name());
					report_saved.setImpression(report.getImpression());
					report_saved.setUv(report.getUv());
					session.update(report_saved);
				}
				
			}
			session.getTransaction().commit();
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			log.error("PublicationReportsTask error", e);
		}
		
	}
	
	private List<PublicationReports> getReports(String date, String sql){
		log.info("PublicationReportsTask Running sql-->" + sql);
		try{
			List<Map<String, Object>> result = hiveJdbcTemplate.queryForList(sql);
			
			Iterator<Map<String,Object>> it = result.iterator();
			List<PublicationReports> reports = new ArrayList<PublicationReports>();
			Session session = HibernateUtil.getSeesion();
			while (it.hasNext()) {
				Map<String, Object> map = it.next();
				PublicationReports report = new PublicationReports();
				if(map.containsKey("campaign_id")){
					report.setCampaign_id(Integer.valueOf(String.valueOf((map.get("campaign_id")))));
					//get campaign name
					Query query_campaign = session.createQuery("from Campaign c where c.id=:id");
					query_campaign.setParameter("id", report.getCampaign_id());
					Campaign campaign = (Campaign) query_campaign.uniqueResult();
					if(campaign!=null){
						report.setCampaign_name(campaign.getName());
					}
				}
				report.setZone_id(Integer.valueOf(String.valueOf(map.get("zone_id"))));
				//get zone by zone_id
				Query query_zone = session.createQuery("from Zone z where z.id=:id");
				query_zone.setParameter("id", report.getZone_id());
				Zone zone = (Zone) query_zone.uniqueResult();
				if(zone!=null){
					report.setZone_name(zone.getName());
					report.setPublication_id(zone.getPublication_id());
				}
				//get publication by publication_name
				Query query_publication = session.createQuery("from Publication p where p.id=:id");
				query_publication.setParameter("id", report.getPublication_id());
				Publication publication = (Publication) query_publication.uniqueResult();
				if(publication!=null){
					report.setPublication_name(publication.getName());
				}
				report.setDate(date);
				report.setImpression(Integer.valueOf(String.valueOf(map.get("impression"))));
				report.setUv(Integer.valueOf(String.valueOf(map.get("uv"))));
				reports.add(report);
			}
			session.close();
			return reports;
		}catch(Exception e){
			e.printStackTrace();
			log.error("PublicationReportsTask error", e);
			return null;
		}
		
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
