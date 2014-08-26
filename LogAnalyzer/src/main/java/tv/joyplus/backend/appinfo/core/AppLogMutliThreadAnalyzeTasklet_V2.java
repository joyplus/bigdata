package tv.joyplus.backend.appinfo.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.config.ConfigManager;
import tv.joyplus.backend.utils.FormatTool;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zino on 14-8-26.
 */
public class AppLogMutliThreadAnalyzeTasklet_V2 implements Tasklet {

    private static Log log = LogFactory.getLog(AppLogMutliThreadAnalyzeTasklet_V2.class);
    @Autowired
    private AppLogAnalyzeDao analyzerFileInfoDao;
    @Autowired
    private ConfigManager configManager;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            if(!AnalyzeOneLog(analyzerFileInfoDao.getUnAnalyzeOne())){Thread.sleep(1000);}
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return RepeatStatus.CONTINUABLE;
    }
    private boolean AnalyzeOneLog(AppLogAnalyzeInfo info){
        if(info == null)return false;//DB no have un-analyze log info
        if(!configManager.containsBusinessId(info.getBusinessId())){//this app not authority to process this businessId info.
            analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_UNKNOW);//for don't analyze by other thread.
            return false;
        }
        String tableName = configManager.getUseableTable(info.getBusinessId());
        if(FormatTool.isEmpty(tableName))return false;//no table to use for save this info.
        //InitThreadPool(); //bc thread pool can't be use to control thread running.
        analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_PROCESSING);//for don't analyze by other thread.
        //threadPoolTaskExecutor.execute(new AnalyzeRunnable(info,tableName));//why we can't use thread pool to control thread running????
        new Thread(new AnalyzeRunnable(info,tableName) ).start();//use this is the same to control mutli thread running
        return true;
    }
    private class AnalyzeRunnable implements Runnable{
        private AppLogAnalyzeInfo appLogAnalyzeInfo;
        private String  tableName;
        public AnalyzeRunnable(AppLogAnalyzeInfo info ,String tableName){
            appLogAnalyzeInfo = info;
            this.tableName    = tableName;
        }
        @Override
        public void run() {
            AnalyzeOne();//here should be have a "timeout" mechanism. for make sure no blocking.
            configManager.releaseTable(appLogAnalyzeInfo.getBusinessId(),tableName);//release table for other threads
        }
        private void AnalyzeOne(){
            log.info("******** MutliThread AnalyzeOne  Jas Start "+tableName+" and path="+appLogAnalyzeInfo.getId()+"************");
            Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
            jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
            jobParametersMap.put("path", new JobParameter(appLogAnalyzeInfo.getPath()));
            jobParametersMap.put("business.id", new JobParameter(appLogAnalyzeInfo.getBusinessId()));
            jobParametersMap.put("tabName",new JobParameter(tableName));
            log.debug("analyze start ->" + appLogAnalyzeInfo.getId());
            try {
                JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));
                while (jobExecution.isRunning()) {
                    Thread.sleep(10);
                }
                log.debug("analyze end   ->" + appLogAnalyzeInfo.getId());
                analyzerFileInfoDao.updateStatus(appLogAnalyzeInfo.getId(), AppLogAnalyzeInfo.STATUS_PROCESSED);
            } catch (Throwable e) {
                analyzerFileInfoDao.updateStatus(appLogAnalyzeInfo.getId(), AppLogAnalyzeInfo.STATUS_FAIL);
            }
        }
    }
    private void InitThreadPool(){
        if(threadPoolTaskExecutor!=null) {
            threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
            threadPoolTaskExecutor.setQueueCapacity(200);
            //threadPoolTaskExecutor.setCorePoolSize(1);
            threadPoolTaskExecutor.setMaxPoolSize(10);
            threadPoolTaskExecutor.setKeepAliveSeconds(30000);
            threadPoolTaskExecutor.initialize();
        }
    }
    //////////////////////////////////////////////////////////////////////////
    private Job job;
    public void setJob(Job job) {
        this.job = job;
    }
    private JobLauncher jobLauncher;
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }
}
