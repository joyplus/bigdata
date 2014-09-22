package tv.joyplus.backend.appinfo.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class AppLogDecision implements JobExecutionDecider {
    private static final Log log = LogFactory.getLog(AppLogDecision.class);
    private static final String INPUT_FILE_PATH = "input.file.path";
    private static final String INPUT_FILE_ID = "input.file.id";
    private static final String INPUT_FILE_PROPERTIES = "input.file.properties";
    private static final String INPUT_BUSINESS_ID = "input.business.id";
    private Queue<AppLogAnalyzeInfo> inputFiles;
    @Autowired
    private AppLogAnalyzeDao analyzerFileInfoDao;
    ExecutionContext context = null;
    Properties prop = new Properties();
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if(context == null) context = jobExecution.getExecutionContext();
        log.debug("AppLogDecision AppLogDecision");
        //加载所有待解析目录
        if (!context.containsKey(INPUT_FILE_PATH)) {

            List<AppLogAnalyzeInfo> files = analyzerFileInfoDao.listUnAnalyzed();
            inputFiles = new LinkedBlockingQueue<AppLogAnalyzeInfo>(files);
            log.debug("FlowExecutionStatus -------INPUT_FILE_PATH---"+(inputFiles.size()));
        }


        //如果有已解析的文件，更新数据库状态为已解析
        if (context.containsKey(INPUT_FILE_PATH)) {
            log.debug("update id:" + context.getLong(INPUT_FILE_ID));
            analyzerFileInfoDao.updateStatus(context.getLong(INPUT_FILE_ID), AnalyzerFileInfo.STATUS_PROCESSED);
            log.debug("FlowExecutionStatus -------"+(inputFiles.size()));
        }
        prop.clear();
        //prop = new Properties();
        while (true) {
            AppLogAnalyzeInfo file = inputFiles.poll();
            if (file != null) {
                try {
                    prop.load(new FileInputStream(new File(file.getPath() + "/DevicesInfo")));
                    log.debug("poll one:" + file.getPath());
                    context.put(INPUT_FILE_PATH, "file:" + file.getPath() + "/*.log");
                    context.put(INPUT_FILE_PROPERTIES, prop);
                    context.put(INPUT_FILE_ID, file.getId());
                    context.put(INPUT_BUSINESS_ID, file.getBusinessId());
                    //System.gc();
                    return FlowExecutionStatus.UNKNOWN;
                } catch (Exception e) {
                    log.debug("loading DevicesInfo properties failed!");
                    analyzerFileInfoDao.updateStatus(file.getId(), AnalyzerFileInfo.STATUS_PROCESSED);
                }
            } else {
                break;
            }
        }
        log.debug("poll completed");
        inputFiles = null;
        context.clearDirtyFlag();
        return FlowExecutionStatus.COMPLETED;
    }
}
