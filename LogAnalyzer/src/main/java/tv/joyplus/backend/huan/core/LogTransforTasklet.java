package tv.joyplus.backend.huan.core;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.huan.beans.LogInfo;
import tv.joyplus.backend.huan.beans.OperationType;
import tv.joyplus.backend.huan.dao.LogDataDao;
import tv.joyplus.backend.huan.dao.LogInfoDao;
import tv.joyplus.backend.huan.dao.LogProcessDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogTransforTasklet implements Tasklet {
    private final static Log log = LogFactory.getLog(LogTransforTasklet.class);

    @Autowired
    private LogDataDao logDataDao;
    @Autowired
    private LogInfoDao logInfoDao;
    @Autowired
    private LogProcessDao logProcessDao;
    private String businessId;

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) {

        List<LogInfo> list = logInfoDao.findLogInfo();
        log.debug("find loginfos :" + list.size());
        for (LogInfo info : list) {
            long campaignId = logInfoDao.getCampaignId(info.getCreativeId());
            long publicationId = logInfoDao.getPublicationId(info.getZoneId());
            long maxId = transfor(info, campaignId, publicationId);
            //更新统计过的最大id
            updateInfoMaxId(info, maxId);

        }

        return RepeatStatus.FINISHED;
    }

    private long transfor(LogInfo info, long campaignId, long publicationId) {
        long maxId = 0;
        String sql = null;
        if (info.getMaxId() == 0) {
            sql = "SELECT * FROM md_log_data WHERE title=? AND imgurl=? AND zone_id=?";
        } else {
            sql = "SELECT * FROM md_log_data WHERE title=? AND imgurl=? AND zone_id=? AND id>" + info.getMaxId();
        }
        Connection connection = logDataDao.GetConnection();

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(Integer.MIN_VALUE);
            statement.setString(1, info.getTitle());
            statement.setString(2, info.getImgurl());
            statement.setLong(3, info.getZoneId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long zoneId = resultSet.getLong("zone_id");

                Map<String, Object> request = new HashMap<String, Object>();
                Map<String, Object> reporting = new HashMap<String, Object>();
                request.put("equipment_sn", "");
                request.put("equipment_key", resultSet.getString("equipment_key"));
                request.put("device_name", resultSet.getString("device_name"));
                reporting.put("device_name", resultSet.getString("device_name"));
                request.put("user_pattern", "");
                request.put("date", resultSet.getTimestamp("ad_date").getTime() / 1000);
                reporting.put("timestamp", resultSet.getTimestamp("ad_date").getTime() / 1000);

                String title = resultSet.getString("title");
                String imgurl = resultSet.getString("imgurl");
                if (title == null || title.length() == 0 || imgurl == null || imgurl.length() == 0) {
                    request.put("operation_type", OperationType.TYPE_AD_NO);
                    reporting.put("impressions", 0);
                } else {
                    request.put("operation_type", OperationType.TYPE_IMPRESSION);
                    reporting.put("impressions", 1);
                }
                reporting.put("requests", 1);

                request.put("operation_extra", "");
                request.put("publication_id", publicationId);
                reporting.put("publication_id", publicationId);
                request.put("zone_id", zoneId);
                reporting.put("zone_id", zoneId);
                request.put("campaign_id", campaignId);
                reporting.put("campaign_id", campaignId);
                request.put("creative_id", info.getCreativeId());
                reporting.put("creative_id", info.getCreativeId());
                request.put("client_ip", resultSet.getString("ip"));
                reporting.put("ip", resultSet.getString("ip"));
                request.put("business_id", businessId);

                reporting.put("report_hash", DigestUtils.md5Hex(reporting.toString()));

                long id = resultSet.getLong("id");
                maxId = id > maxId ? id : maxId;
                logProcessDao.sendToRequestLog(request);
                logProcessDao.sendToReporting(reporting);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            if (statement != null) try {
                statement.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        return maxId;
    }

    private void updateInfoMaxId(LogInfo info, long id) {
        if (id == 0)
            return;
        info.setMaxId(id);
        logInfoDao.updateMaxId(info);
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

}
