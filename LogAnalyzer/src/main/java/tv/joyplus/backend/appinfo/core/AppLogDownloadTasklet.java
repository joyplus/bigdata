package tv.joyplus.backend.appinfo.core;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.Config;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.beans.AppLogDownloadInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.appinfo.dao.AppLogDownloadDao;
import tv.joyplus.backend.config.ConfigManager;
import tv.joyplus.backend.qiniu.QiniuManager;

import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;

public class AppLogDownloadTasklet implements Tasklet {
    private static Log log = LogFactory.getLog(AppLogDownloadTasklet.class);

    @Autowired
    private QiniuManager qiniu;
    @Autowired
    private AppLogDownloadDao downloadDao;
    @Autowired
    private AppLogAnalyzeDao analyzeDao;
    @Autowired
    private ConfigManager configManager;
    private String unzipPassword;

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {
        log.debug("download repeat start");
        try {
            start();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("download repeat end");
        return RepeatStatus.CONTINUABLE;
    }

    private void start() throws Exception {
        //Runtime.getRuntime().gc();
        //获取一个将要下载的文件信息
        AppLogDownloadInfo info = downloadDao.get();
        if (info == null) {
            //延迟一段时间再从数据库查询是否有新的文件需要下载
            Thread.sleep(1000);
            return;
        }

        File root = new File(configManager.getConfiguration(
                Config.LocationKey.AP_LOG_DOWNLOAD_DIR, info.getBusinessId()));
        try {
            FileUtils.forceMkdir(root);
        } catch (Exception e) {
        }

        //更新状态为下载中
        downloadDao.updateStatus(info.getId(), AppLogDownloadInfo.STATUS_DOWNLOADING);

        log.debug("this :" + info.getFilename() + " will be download!");
        //下载文件
        String url = qiniu.downloadUrl(info.getBusinessId(), info.getIdent()); //生成下载链接
        log.debug("download url->" + url);
        qiniu.download(info.getBusinessId(), info.getIdent(), new File(root, info.getFilename()));

        //更新状态为已下载
        downloadDao.updateStatus(info.getId(), AppLogDownloadInfo.STATUS_DOWNLOADED);

        //解压文件
        unzip(info);
    }

    /**
     * 解压文件
     *
     * @param info
     * @throws Exception
     */
    private void unzip(AppLogDownloadInfo info) {
        File root = new File(configManager.getConfiguration(Config.LocationKey.AP_LOG_DIR, info.getBusinessId()), info.getBusinessId());
        String filename = info.getFilename();
        File saveDir = new File(root, filename);
        saveDir.mkdirs();

        try {
            File file = new File(info.getPath() + info.getFilename());
            unzip(saveDir, file);
            addToAnalyzerTable(saveDir.getPath(), AppLogAnalyzeInfo.STATUS_UNPROCESSE, info.getBusinessId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 解压zip文件
     *
     * @param saveDir
     * @param zipFile
     */
    private void unzip(File saveDir, File zipFile) throws Exception {
        log.debug("unzip begin save path:" + saveDir.getPath());
        ZipFile zFile = new ZipFile(zipFile);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("bad zip file");
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(unzipPassword);
        }
        zFile.extractAll(saveDir.getPath());
    }

    /**
     * 解压文件信息存入数据库
     *
     * @param path
     * @param status
     */
    private void addToAnalyzerTable(String path, int status, String businessId) {
        AppLogAnalyzeInfo info = new AppLogAnalyzeInfo();
        info.setPath(path);
        info.setStatus(status);
        info.setCreate_time(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        info.setBusinessId(businessId);
        analyzeDao.save(info);
    }

    public void setUnzipPassword(String unzipPassword) {
        this.unzipPassword = unzipPassword;
    }
}
