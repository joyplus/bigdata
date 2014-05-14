package tv.joyplus.backend.huan.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class LogDecision implements JobExecutionDecider {
	private static final String INPUT_DIR = "input.dir";
	private static final String INPUT_FILE = "input.file";
	private Queue<String> inputFiles;

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if(!jobExecution.getExecutionContext().containsKey(INPUT_FILE)) {
			File dir = new File(jobExecution.getJobParameters().getString(INPUT_DIR));
			if(!dir.isDirectory()) {
				
			}
			String[] files = dir.list();
			List<String> list = new ArrayList<String>();
			for(String filename : files) {
				list.add("file:"+dir.getAbsolutePath()+"/"+filename);
			}
			inputFiles = new LinkedBlockingQueue<String>(list);
		}
		String filepath = inputFiles.poll();
		if(filepath!=null) {
			jobExecution.getExecutionContext().put("INPUT_FILE", filepath);
            return FlowExecutionStatus.UNKNOWN;
		}
		return FlowExecutionStatus.COMPLETED;
	}

}
