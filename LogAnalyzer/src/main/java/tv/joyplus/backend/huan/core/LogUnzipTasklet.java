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
	private static int bufferSize = 1024 * 10;
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
		try{
			FileUtils.forceMkdir(root);
		}catch(Exception e){}
		List<DownloadFileInfo> list = downloadFileInfoDao.listUnzip();
		for(DownloadFileInfo info : list) {
			//未解压
			if(info.getZip()==0) {
				log.debug("Will unzip :"+info.getIdent());
				File unzipfile = new File(new File(info.getPath()), info.getFilename());
				if(mimeCompress.containsKey(info.getMimeType())) {
					File tmpTarfile = deCompress(unzipfile, mimeCompress.get(info.getMimeType()));
					deArchive(tmpTarfile, "tar", true);
				}else if(mimeArchive.containsKey(info.getMimeType())) {
					deArchive(unzipfile, mimeArchive.get(info.getMimeType()), false);
				}
			}
			downloadFileInfoDao.updateZip(info);
			
		}
		return RepeatStatus.FINISHED;
	}

	private void deArchive(File file, String mimeType, boolean deleteFile) throws Exception {
		File root = new File(unzipDir);
		ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(mimeType, new FileInputStream(file));
		while (true) {  
            ArchiveEntry entry = in.getNextEntry();
            if(entry==null) {
            	break;
            }
            if(entry.isDirectory()) {
            	(new File(root, entry.getName())).mkdirs();
            }else{
            	File f = new File(root, entry.getName());
            	if (!f.exists()) {  
                    f.createNewFile();  
                }
            	FileOutputStream out = new FileOutputStream(f);
            	byte[] bs = new byte[bufferSize];
            	int len = -1;  
                while ((len = in.read(bs)) != -1) {  
                    out.write(bs, 0, len);  
                }  
                out.flush();
                log.debug("unanalyzed file:"+f.getAbsolutePath());
                addToAnalyzerTable(f.getAbsolutePath(), f.getName());
                
            }
		}
		if(deleteFile) {
			file.delete();
		}
	}
	private File deCompress(File file, String mimeType) throws Exception {
		log.debug("unCompress "+ mimeType);
		CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream(mimeType, new FileInputStream(file));
		File outFile = new File(file.getParent() + File.separator + "tmp.tar");
		FileOutputStream out = new FileOutputStream(outFile);
		 final byte[] buffer = new byte[bufferSize];
		 int n = 0;  
         while (-1 != (n = in.read(buffer))) {  
             out.write(buffer, 0, n);  
         }  
         return outFile;
	}
	
	private void addToAnalyzerTable(String path, String filename) {
		AnalyzerFileInfo instance = new AnalyzerFileInfo();
		instance.setPath(path);
		instance.setFilename(filename);
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
