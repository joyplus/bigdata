package tv.joyplus.backend.appinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.config.ConfigManager;
import tv.joyplus.backend.utils.FormatTool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLogAnalyzeLauncher {
    private final static Log log = LogFactory.getLog(AppLogAnalyzeLauncher.class);
    private static boolean isRunning = false;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private Job job;
    private Job mutliThreadjob;
    @Autowired
    private AppLogAnalyzeDao analyzerFileInfoDao;
    @Autowired
    private ConfigManager configManager;

    public void execute() {
        log.debug("************************************* AppLogAnalyzeLauncher "+(new Date(System.currentTimeMillis()))+" ************************************************");
        String[] businessId = configManager.getBusinessIds();
        if(businessId==null||businessId.length<=0)return;//this app has no businessId to analyze
        if(configManager.getMultiThreadAnalyzeEn(businessId[0])){//use the first one to judge Mutli or samlpe
            MutliThreadAnalyze();
        }else{
            SampleThreadAnalyze();
        }
        log.debug("************************************* AppLogAnalyzeLauncher "+(new Date(System.currentTimeMillis()))+" ************************************************");
    }
    private void MutliThreadAnalyze(){
        log.debug(" MutliThreadAnalyze  ");
        try {
            jobLauncher.run(mutliThreadjob,new JobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
        } catch (JobRestartException e) {
        } catch (JobInstanceAlreadyCompleteException e) {
        } catch (JobParametersInvalidException e) {
        }
    }
    private void SampleThreadAnalyze(){
        log.debug(" SampleThreadAnalyze  ");
        if (isRunning) {
            log.info("previews job doesn't stopped, this job will exit");
            return;
        }
        try {
            log.info("job start");
            isRunning = true;
            log.info("previews job stopped, this job will start");
            List<AppLogAnalyzeInfo> infos = analyzerFileInfoDao.listUnAnalyzed();
            AppLogAnalyzeInfo info = null;
            for (int i = 0; i < infos.size(); i++) {
                info = infos.get(i);
                AnalyzeOne(info);
            }
            log.info("job done");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        isRunning = false;
    }
    private void AnalyzeOne(AppLogAnalyzeInfo info){
        if(info == null )return;
        if(FormatTool.isEmpty(configManager.getSampleThreadTable(info.getBusinessId()))){
            analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_UNKNOW);
            return;
        }
        Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
        jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
        jobParametersMap.put("path", new JobParameter(info.getPath()));
        jobParametersMap.put("business.id", new JobParameter(info.getBusinessId()));
        jobParametersMap.put("tabName",new JobParameter(configManager.getSampleThreadTable(info.getBusinessId())));
        log.debug("analyze start ->" + info.getId());
        try {
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));
            while (jobExecution.isRunning()) {
                Thread.sleep(10);
            }
            log.debug("analyze end   ->" + info.getId());
            analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_PROCESSED);
        } catch (Throwable e) {
            analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_FAIL);
            return;
        }
    }
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setMutliThreadjob(Job mutliThreadjob) {
        this.mutliThreadjob = mutliThreadjob;
    }
}