package tv.joyplus.backend.huan.core;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;
import tv.joyplus.backend.huan.dao.AnalyzerFileInfoDao;

public class LogDecision implements JobExecutionDecider {
	private static final Log log = LogFactory.getLog(LogDecision.class);
	private static final String INPUT_FILE = "input.file";
	private static final String INPUT_FILE_ID = "input.file.id";
	private static final String INPUT_FILE_NAME = "input.file.name";
	private Queue<AnalyzerFileInfo> inputFiles;
	@Autowired
	private AnalyzerFileInfoDao analyzerFileInfoDao;
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		ExecutionContext context = jobExecution.getExecutionContext();
		log.debug(context);
		//加载所有待解析的log文件
		if(!jobExecution.getExecutionContext().containsKey(INPUT_FILE)) {
			List<AnalyzerFileInfo> files = analyzerFileInfoDao.listUnAnalyzed();
			inputFiles = new LinkedBlockingQueue<AnalyzerFileInfo>(files);
		}
		AnalyzerFileInfo file = inputFiles.poll();
		
		//如果有已解析的文件，更新数据库状态为已解析
		if(context.containsKey(INPUT_FILE_ID)) {
			log.debug("update id:" + context.getLong(INPUT_FILE_ID));
			analyzerFileInfoDao.updateStatus(context.getLong(INPUT_FILE_ID), AnalyzerFileInfo.STATUS_PROCESSED);
		}
		if(file!=null) {
			log.debug("poll one:"+file.getPath());
			context.put(INPUT_FILE, "file:"+file.getPath());
			context.put(INPUT_FILE_ID, file.getId());
			context.put(INPUT_FILE_NAME, file.getFilename());
            return FlowExecutionStatus.UNKNOWN;
		}
		log.debug("poll complated");
		return FlowExecutionStatus.COMPLETED;
	}

}
