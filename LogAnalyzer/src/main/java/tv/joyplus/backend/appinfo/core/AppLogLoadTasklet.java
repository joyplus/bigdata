package tv.joyplus.backend.appinfo.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;
import tv.joyplus.backend.qiniu.QiniuItem;
import tv.joyplus.backend.qiniu.dao.QiniuDao;
import tv.joyplus.backend.utils.FormatTool;

public class AppLogLoadTasklet implements Tasklet {
	private static Log log = LogFactory.getLog(AppLogLoadTasklet.class);
    private static final int BEFORE_SIZE = 2;
    private static final int BEFORE_DURATION = 10*60*1000;
	@Autowired
	private QiniuDao qiniuDao;
	@Autowired
	private AppLogDownloadDao downloadDao;
	@Autowired
	private AppLogAnalyzeDao analyzeDao;
	private String downloadDir;
	private long time;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		// 待下载文件列表
		log.debug("time->"+time+";date->"+new Date(time));
        for(int i=0; i<BEFORE_SIZE; i++) {
            String prifix = FormatTool.date("yyyy-MM-dd-HH-mm", new Date(time-(BEFORE_DURATION*(i+1))));
            prifix = prifix.substring(0, prifix.length() - 1);
            log.debug("prifix-> " + prifix);
            List<QiniuItem> list = qiniuDao.list(prifix);
            List<AppLogDownloadInfo> infoList = new ArrayList<AppLogDownloadInfo>();
            for (QiniuItem item : list) {
                if (downloadDao.existIdent(item.getKey())) {
                    continue;
                }
                AppLogDownloadInfo info = newAppLogDownloadInfo(item);
                infoList.add(info);
            }
            downloadDao.batchSave(infoList);
        }
		log.info("log load tasklet done");
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private AppLogDownloadInfo newAppLogDownloadInfo(QiniuItem item) throws Exception {
		AppLogDownloadInfo info = new AppLogDownloadInfo();
		info.setIdent(item.getKey());
		info.setFilename(qiniuDao.formatFilename(item.getKey()));
		info.setMimeType(item.getMimeType());
		info.setPath(downloadDir);
		info.setUrl(qiniuDao.downloadUrl(item.getKey()));
		info.setPutTime(item.getPutTime()/10000000);
		info.setSize(item.getFsize());
		info.setStatus(0);
		info.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		return info;
	}
	
	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
