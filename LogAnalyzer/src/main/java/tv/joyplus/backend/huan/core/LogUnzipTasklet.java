package tv.joyplus.backend.huan.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;
import tv.joyplus.backend.huan.beans.DownloadFileInfo;
import tv.joyplus.backend.huan.dao.AnalyzerFileInfoDao;
import tv.joyplus.backend.huan.dao.DownloadFileInfoDao;

public class LogUnzipTasklet implements Tasklet {
	private static Log log = LogFactory.getLog(LogUnzipTasklet.class);
	private String unzipDir;
	private Map<String, String> mimeArchive;
	private Map<String, String> mimeCompress;
	@Autowired
	private DownloadFileInfoDao downloadFileInfoDao;
	@Autowired
	private AnalyzerFileInfoDao analyzerFileInfoDao;
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
			throws Exception {
		File root = new File(unzipDir);
		root.mkdirs();
		
		File saveDir = null;
		//获取未解压的文件
		List<DownloadFileInfo> list = downloadFileInfoDao.listUnzip();
		for(DownloadFileInfo info : list) {
			int index = info.getIdent().lastIndexOf("/");
			if(index != -1) {
				String prefix = info.getIdent().substring(0, index);
				saveDir = new File(root, prefix);
			}else{
				saveDir = root;
			}
			log.debug("Will unzip :" + info.getIdent());
			saveDir.mkdirs();
			
			File unzipfile = new File(new File(info.getPath()), info.getFilename());
			//先解压成tar文件
			if(mimeCompress.containsKey(info.getMimeType())) {
				deCompress(saveDir, unzipfile, mimeCompress.get(info.getMimeType()));
			}else if(mimeArchive.containsKey(info.getMimeType())) {
				deArchive(saveDir, unzipfile, mimeArchive.get(info.getMimeType()));
			}
			//标记为已解压
			downloadFileInfoDao.updateZip(info);
		}
		return RepeatStatus.FINISHED;
	}

	/**
	 * 解压tar,zip文件
	 * @param file
	 * @param archiveType
	 * @throws Exception
	 */
	private void deArchive(File saveDir, File file, String archiveType) throws Exception {
		ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(archiveType, new FileInputStream(file));
		ArchiveEntry entry = null;
		while ((entry=in.getNextEntry())!=null) {  
            if(entry.isDirectory()) {
            	new File(saveDir, entry.getName()).mkdirs();
            }else{
            	File f = new File(saveDir, entry.getName());
            	//文件存在
            	if(f.exists()) {
            		//已存在的文件放入临时目录
            		File existDir = new File(saveDir, "log.file.exist.tmp");
            		existDir.mkdirs();
            		File exist = new File(existDir, entry.getName());
            		FileOutputStream out = new FileOutputStream(exist);
                	IOUtils.copy(in, out);
                	addToAnalyzerTable(exist.getAbsolutePath(), exist.getName(), AnalyzerFileInfo.STATUS_EXIST);
                	log.error(exist.getName()+" existed");
            	}else{
            		FileOutputStream out = new FileOutputStream(f);
                	IOUtils.copy(in, out);
                    log.debug("unanalyzed file:"+f.getAbsolutePath());
                    addToAnalyzerTable(f.getAbsolutePath(), f.getName(), AnalyzerFileInfo.STATUS_UNPROCESSE);
            	}
            }
		}
	}
	/**
	 * 解压gz,bz2
	 * @param file
	 * @param archiveType
	 * @return
	 * @throws Exception
	 */
	private void deCompress(File saveDir, File file, String archiveType) throws Exception {
		CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(archiveType, new FileInputStream(file));
		File outFile = new File(file.getParent() + File.separator + "tmp.tar");
		FileOutputStream out = new FileOutputStream(outFile);
		IOUtils.copy(in, out);
        deArchive(saveDir, outFile, "tar");
        outFile.delete();
	}
	
	/**
	 * 解压文件信息存入数据库
	 * @param path
	 * @param filename
	 */
	private void addToAnalyzerTable(String path, String filename, int status) {
		AnalyzerFileInfo instance = new AnalyzerFileInfo();
		instance.setPath(path);
		instance.setFilename(filename);
		instance.setStatus(status);
		instance.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		analyzerFileInfoDao.save(instance);
	}
	public void setUnzipDir(String unzipDir) {
		this.unzipDir = unzipDir;
	}
	public void setMimeArchive(Map<String, String> mimeArchive) {
		this.mimeArchive = mimeArchive;
	}
	public void setMimeCompress(Map<String, String> mimeCompress) {
		this.mimeCompress = mimeCompress;
	}
	
}
