package tv.joyplus.backend.report.task.impl;

import java.util.List;

import org.springframework.core.task.TaskExecutor;

import tv.joyplus.backend.report.dao.JobResultDao;
import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;
import tv.joyplus.backend.report.task.ReportTask;

public class ReportTaskImpl implements ReportTask {
	
    private TaskExecutor taskExecutor;
    
	private JobResultDao jobResultDao;
	
	private ProcessDao processDao;
	
	public JobResultDao getJobResultDao() {
		return jobResultDao;
	}

	public void setJobResultDao(JobResultDao jobResultDao) {
		this.jobResultDao = jobResultDao;
	}

	public ProcessDao getProcessDao() {
		return processDao;
	}

	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
	}
	
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	
	private class ProcessQuery implements Runnable {

        private String message;

        public ProcessQuery(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(message);
        }

    }
    
    public ReportTaskImpl() {

    }

    public ReportTaskImpl(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void printMessages() {

    }

	public String getJob() {
		// TODO Auto-generated method stub
		return null;
	}

//	public JobResultDto queryData(String strJobText) {
//		
//		//sample
//        for(int i = 0; i < 25; i++) {
//            taskExecutor.execute(new ProcessQuery("Message" + i));
//        }
//        
//        return null;
//	}

	private ParameterDto parseParameter (String jsonString) {
		
		return null;
	}

	private List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		return this.getProcessDao().queryData(parameterDto);
	}

	public void processReport(String jsonString) {
		ParameterDto parameterDto = this.parseParameter(jsonString);
		List<JobResultDto> results = this.queryData(parameterDto);
		this.getJobResultDao().saveJobResults(results);
		this.getJobResultDao().updateReportStatus(parameterDto.getReportId());
	}



}
