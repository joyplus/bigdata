package tv.joyplus.backend.appinfo.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.Config;
import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.beans.AppLogProcessInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;
import tv.joyplus.backend.appinfo.dao.AppLogProcessDao;
import tv.joyplus.backend.config.ConfigManager;
import tv.joyplus.backend.qiniu.QiniuItem;
import tv.joyplus.backend.qiniu.QiniuManager;
import tv.joyplus.backend.utils.FormatTool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppLogLoadTasklet implements Tasklet {
    private static Log log = LogFactory.getLog(AppLogLoadTasklet.class);
    private static final int BEFORE_SIZE = 2;
    private static final int BEFORE_DURATION = 10 * 60 * 1000;
    @Autowired
    private QiniuManager qiniu;
    @Autowired
    private AppLogDownloadDao downloadDao;
    @Autowired
    private AppLogProcessDao processDao;
    @Autowired
    private ConfigManager configManager;
    private long time;
    private String[] businessIds;

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {
        for(String businessId : businessIds) {
            AppLogProcessInfo info = getLastTime(businessId);

            // 待下载文件列表
            log.debug("time->" + time + ";date->" + new Date(time));

            for (long i = info.getLastExecuteTime().getTime(); i < (time - BEFORE_DURATION); i += BEFORE_DURATION) {
                downloadInfoList(businessId, new Date(i));
                info.setLastExecuteTime(new Timestamp(i));
                processDao.save(info);
            }
        }
        log.info("log load tasklet done");
        return RepeatStatus.FINISHED;
    }

    private AppLogProcessInfo getLastTime(String businessId) {
        AppLogProcessInfo info = processDao.lastTime(businessId);
        if(info==null) {
            info = new AppLogProcessInfo();
            info.setBusinessId(businessId);
            info.setLastExecuteTime(new Timestamp(System.currentTimeMillis() - (BEFORE_SIZE * BEFORE_DURATION)));
        }
        return info;
    }

    private void downloadInfoList(String businessId, Date date) throws Exception {
        String prifix = FormatTool.date("yyyy-MM-dd-HH-mm", date);
        prifix = prifix.substring(0, prifix.length() - 1);
        log.debug("prifix-> " + prifix);
        List<QiniuItem> list = qiniu.list(businessId, prifix);
        //add by Jas@20140731 for avoid null exception.
        if(list==null||list.size()<=0)return;
        //end add by Jas
        log.debug("business.id -> " + businessId + ", size ->" + list.size());
        List<AppLogDownloadInfo> infoList = new ArrayList<AppLogDownloadInfo>();
        for (QiniuItem item : list) {
            if (downloadDao.existIdent(item.getKey(), businessId)) {
                continue;
            }
            AppLogDownloadInfo info = newAppLogDownloadInfo(item, businessId);
            infoList.add(info);
        }
        downloadDao.batchSave(infoList);
    }

    /**
     *
     * @param item
     * @param businessId
     * @return
     * @throws Exception
     */
    private AppLogDownloadInfo newAppLogDownloadInfo(QiniuItem item, String businessId) throws Exception {
        AppLogDownloadInfo info = new AppLogDownloadInfo();
        info.setIdent(item.getKey());
        info.setFilename(qiniu.formatFilename(item.getKey()));
        info.setMimeType(item.getMimeType());
        info.setPath(configManager.getConfiguration(Config.LocationKey.AP_LOG_DOWNLOAD_DIR, businessId));
        info.setUrl(qiniu.downloadUrl(businessId, item.getKey()));
        info.setPutTime(item.getPutTime() / 10000000);
        info.setSize(item.getFsize());
        info.setStatus(0);
        info.setCreateTime(new Timestamp(System.currentTimeMillis()));
        info.setBusinessId(businessId);

        return info;
    }


    public void setTime(long time) {
        this.time = time;
    }

    public void setBusinessIds(String businessIds) {
        this.businessIds = businessIds.split(",");
    }
}
