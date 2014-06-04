package tv.joyplus.backend.report.task.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import tv.joyplus.backend.report.dao.JobResultDao;
import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dao.impl.ProcessDaoImpl;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;
import tv.joyplus.backend.report.exception.ReportBaseException;
import tv.joyplus.backend.report.jsonparse.ReportParser;
import tv.joyplus.backend.report.task.ReportTask;
import tv.joyplus.backend.utility.Const;

public class ReportTaskImpl implements ReportTask {
	
    private TaskExecutor taskExecutor;
    
	private JobResultDao jobResultDao;
	
	private ProcessDao processDao;
	
	private ReportParser jsonParser;
	Log log = LogFactory.getLog(ProcessDaoImpl.class);
	
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
	
	
	public ReportParser getJsonParser() {
		return jsonParser;
	}

	public void setJsonParser(ReportParser jsonParser) {
		this.jsonParser = jsonParser;
	}


	private class ProcessQuery implements Runnable {

        private String message;

        public ProcessQuery(String message) {
            this.message = message;
        }

        public void run() {
        	ParameterDto parameterDto = null;
        	List<JobResultDto> results = null;
        	try {
        		parameterDto = parseParameter(message);
        		results = queryData(parameterDto);
        		getJobResultDao().saveJobResults(results);
        		log.info("Report " + parameterDto.getReportId() + " generate success");
            	getJobResultDao().updateReportStatus(parameterDto.getReportId(),Const.RESULT_STATUS_SUCCESS);
			} catch (ReportBaseException e) {
				// TODO: handle exception
				log.error("ReportBaseException id : " + e.getExceptionId() + "\t error message :" + e.getErrorMessage() + "\t caseBy :" + e.getException());
				if(parameterDto!=null){
					getJobResultDao().updateReportStatus(parameterDto.getReportId(),Const.RESULT_STATUS_FAILE);
					log.error("Report " + parameterDto.getReportId() + " generate faile");
				}
			}catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage());
				if(parameterDto!=null){
					getJobResultDao().updateReportStatus(parameterDto.getReportId(),Const.RESULT_STATUS_FAILE);
					log.error("Report " + parameterDto.getReportId() + " generate faile");
				}
			}
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
		ParameterDto parameterDto = null;
		try {
			parameterDto =  jsonParser.parseParameter(jsonString);
		} catch (Exception e) {
			throw new ReportBaseException(Const.EXCEPTION_JSONPARSE, "json parse faile : " + jsonString ,e);
		}
		return parameterDto;
	}

	private List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		return this.getProcessDao().queryData(parameterDto);
	}

	public void processReport(String jsonString) {
		taskExecutor.execute(new ProcessQuery(jsonString));
	}
}
