package tv.joyplus.backend.huan.core;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.DownloadFileInfo;
import tv.joyplus.backend.huan.dao.DownloadFileInfoDao;
import tv.joyplus.backend.qiniu.QiniuItem;
import tv.joyplus.backend.qiniu.dao.QiniuDao;

public class LogLoadTasklet implements Tasklet {
	private static Log log = LogFactory.getLog(LogLoadTasklet.class);
	@Autowired
	private QiniuDao qiniuDao;
	@Autowired
	private DownloadFileInfoDao downloadFileInfoDao;
	
	private String downloadDir;
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
			throws Exception {
		File root = new File(downloadDir);
		try {
			FileUtils.forceMkdir(root);
		}catch(Exception e){}
		//已下载文件列表
		List<String> existFileList = downloadFileInfoDao.listAllIdent();
		//待下载文件列表
		List<QiniuItem> list = qiniuDao.list("");
		for(QiniuItem item : list) {
			if(existFileList.contains(item.getKey())) {
				continue;
			}
			log.debug("this :"+item.getKey()+" will be download!");
			
			DownloadFileInfo downloadFileInfo = new DownloadFileInfo();
			downloadFileInfo.setIdent(item.getKey());
			downloadFileInfo.setFilename(qiniuDao.formatFilename(item.getKey()));
			downloadFileInfo.setMimeType(item.getMimeType());
			downloadFileInfo.setPath(downloadDir);
			downloadFileInfo.setUrl(qiniuDao.downloadUrl(item.getKey()));
			downloadFileInfo.setStatus(0);
			downloadFileInfo.setZip(0);
			downloadFileInfo.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
			qiniuDao.download(downloadFileInfo.getUrl(), new File(root, downloadFileInfo.getFilename()));
			downloadFileInfoDao.save(downloadFileInfo);
		}
		log.info("log load tasklet done");
		return RepeatStatus.FINISHED;
	}
	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}
	
}
