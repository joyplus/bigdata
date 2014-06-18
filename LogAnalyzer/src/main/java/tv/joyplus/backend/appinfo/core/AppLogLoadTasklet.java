package tv.joyplus.backend.appinfo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;
import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;
import tv.joyplus.backend.huan.beans.DownloadFileInfo;
import tv.joyplus.backend.huan.dao.DownloadFileInfoDao;
import tv.joyplus.backend.qiniu.QiniuItem;
import tv.joyplus.backend.qiniu.dao.QiniuDao;

public class AppLogLoadTasklet implements Tasklet {
	private static Log log = LogFactory.getLog(AppLogLoadTasklet.class);
	@Autowired
	private QiniuDao qiniuDao;
	@Autowired
	private AppLogDownloadDao downloadDao;

	private String downloadDir;
	private String unzipDir;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		File root = new File(downloadDir);
		try {
			FileUtils.forceMkdir(root);
		} catch (Exception e) {
		}
		// 已下载文件列表
		List<String> existFileList = downloadDao.listAllIdent();
		// 待下载文件列表
		List<QiniuItem> list = qiniuDao.list();
		for (QiniuItem item : list) {
			if (existFileList.contains(item.getKey())) {
				continue;
			}
			log.debug("this :" + item.getKey() + " will be download!");

			AppLogDownloadInfo downloadFileInfo = new AppLogDownloadInfo();
			downloadFileInfo.setIdent(item.getKey());
			downloadFileInfo
					.setFilename(qiniuDao.formatFilename(item.getKey()));
			downloadFileInfo.setMimeType(item.getMimeType());
			downloadFileInfo.setPath(downloadDir);
			downloadFileInfo.setUrl(qiniuDao.downloadUrl(item.getKey()));
			downloadFileInfo.setPutTime(item.getPutTime());
			downloadFileInfo.setSize(item.getFsize());
			downloadFileInfo.setStatus(0);
			downloadFileInfo.setZip(0);
			downloadFileInfo.setCreateTime(new Timestamp(Calendar.getInstance()
					.getTimeInMillis()));

			qiniuDao.download(downloadFileInfo.getUrl(), new File(root,
					downloadFileInfo.getFilename()));
			downloadDao.save(downloadFileInfo);
		}
		log.info("log load tasklet done");
		return RepeatStatus.FINISHED;
	}

	/**
	 * 解压tar,zip文件
	 * 
	 * @param file
	 * @param archiveType
	 * @throws Exception
	 */
	private void deArchive(File saveDir, File file, String archiveType)
			throws Exception {
		ArchiveInputStream in = new ArchiveStreamFactory()
				.createArchiveInputStream(archiveType,
						new FileInputStream(file));
		ArchiveEntry entry = null;
		while ((entry = in.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				new File(saveDir, entry.getName()).mkdirs();
			} else {
				File f = new File(saveDir, entry.getName());
				// 文件存在
				if (f.exists()) {
					// 已存在的文件放入临时目录
					File existDir = new File(saveDir, "log.file.exist.tmp");
					existDir.mkdirs();
					File exist = new File(existDir, entry.getName());
					FileOutputStream out = new FileOutputStream(exist);
					IOUtils.copy(in, out);
					addToAnalyzerTable(exist.getAbsolutePath(),
							exist.getName(), AnalyzerFileInfo.STATUS_EXIST);
					log.error(exist.getName() + " existed");
				} else {
					FileOutputStream out = new FileOutputStream(f);
					IOUtils.copy(in, out);
					log.debug("unanalyzed file:" + f.getAbsolutePath());
					addToAnalyzerTable(f.getAbsolutePath(), f.getName(),
							AnalyzerFileInfo.STATUS_UNPROCESSE);
				}
			}
		}
	}

	
	/**
	 * 解压文件信息存入数据库
	 * @param path
	 * @param filename
	 */
	private void addToAnalyzerTable(String path, String filename, int status) {
		
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public void setUnzipDir(String unzipDir) {
		this.unzipDir = unzipDir;
	}

}
