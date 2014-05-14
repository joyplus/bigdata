package tv.joyplus.backend.report.task.impl;

import org.springframework.core.task.TaskExecutor;

import tv.joyplus.backend.report.dao.JobResult;
import tv.joyplus.backend.report.task.ReportTask;

public class ReportTaskImpl implements ReportTask {
	
	private class ProcessQuery implements Runnable {

        private String message;

        public ProcessQuery(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(message);
        }

    }

    private TaskExecutor taskExecutor;
    
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

	public JobResult queryData(String strJobText) {
		
		//sample
        for(int i = 0; i < 25; i++) {
            taskExecutor.execute(new ProcessQuery("Message" + i));
        }
        
        return null;
	}

	public boolean saveData(JobResult result) {
		// TODO Auto-generated method stub
		return false;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}


}
