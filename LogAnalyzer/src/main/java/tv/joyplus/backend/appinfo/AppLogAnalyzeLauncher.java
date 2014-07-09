package tv.joyplus.backend.appinfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLogAnalyzeLauncher {
    private final static Log log = LogFactory.getLog(AppLogAnalyzeLauncher.class);
    private static boolean isRunning = false;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private Job job;
    @Autowired
    private AppLogAnalyzeDao analyzerFileInfoDao;
    Map<String, JobParameter> jobParametersMap = new HashMap<>();;

    public void execute() {
        try {
            log.info("job start");
            if(!isRunning) {
                isRunning = true;
                log.info("previews job stopped, this job will start");
                List<AppLogAnalyzeInfo> infos = analyzerFileInfoDao.listUnAnalyzed();
                AppLogAnalyzeInfo info = null;
                for(int i=0; i<infos.size(); i++) {
                    info = infos.get(i);
                    jobParametersMap.put("time", new JobParameter(System.currentTimeMillis()));
                    jobParametersMap.put("path", new JobParameter(info.getPath()));
                    jobParametersMap.put("business.id", new JobParameter(info.getBusinessId()));
                    log.debug("analyze start ->" + info.getId());
                    try {
                        JobExecution jobExecution = jobLauncher.run(job, new JobParameters(jobParametersMap));
                        while (jobExecution.isRunning()) {
                            Thread.sleep(10);
                        }
                    } catch (Throwable e) {}
                    log.debug("analyze end   ->" + info.getId());
                    analyzerFileInfoDao.updateStatus(info.getId(), AppLogAnalyzeInfo.STATUS_PROCESSED);
                }
            }else{
                log.info("previews job doesn't stopped, this job will exit");
            }
            log.info("job done");
            isRunning = false;
        } catch (Exception e) {
            log.error(e.getMessage());
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

}
