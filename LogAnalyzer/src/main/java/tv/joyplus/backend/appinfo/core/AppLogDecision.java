package tv.joyplus.backend.appinfo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
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

import tv.joyplus.backend.appinfo.beans.AppLogAnalyzeInfo;
import tv.joyplus.backend.appinfo.dao.AppLogAnalyzeDao;
import tv.joyplus.backend.huan.beans.AnalyzerFileInfo;

public class AppLogDecision implements JobExecutionDecider {
	private static final Log log = LogFactory.getLog(AppLogDecision.class);
	private static final String INPUT_FILE_PATH = "input.file.path";
	private static final String INPUT_FILE_ID = "input.file.id";
	private static final String INPUT_FILE_PROPERTIES = "input.file.properties";
	private Queue<AppLogAnalyzeInfo> inputFiles;
	@Autowired
	private AppLogAnalyzeDao analyzerFileInfoDao;
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		ExecutionContext context = jobExecution.getExecutionContext();
		//加载所有待解析目录
		if(!context.containsKey(INPUT_FILE_PATH)) {
			List<AppLogAnalyzeInfo> files = analyzerFileInfoDao.listUnAnalyzed();
			inputFiles = new LinkedBlockingQueue<AppLogAnalyzeInfo>(files);
		}
		AppLogAnalyzeInfo file = inputFiles.poll();
		
		//如果有已解析的文件，更新数据库状态为已解析
		if(context.containsKey(INPUT_FILE_PATH)) {
			log.debug("update id:" + context.getLong(INPUT_FILE_ID));
			analyzerFileInfoDao.updateStatus(context.getLong(INPUT_FILE_ID), AnalyzerFileInfo.STATUS_PROCESSED);
		}
		if(file!=null) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(new File(file.getPath() + "/DevicesInfo")));
				log.debug("poll one:"+file.getPath());
				context.put(INPUT_FILE_PATH, "file:"+file.getPath()+"/*.log");
				context.put(INPUT_FILE_PROPERTIES, prop);
				context.put(INPUT_FILE_ID, file.getId());
				return FlowExecutionStatus.UNKNOWN;
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			
			if(!inputFiles.isEmpty()) {
				context.remove(INPUT_FILE_PROPERTIES);
				return FlowExecutionStatus.UNKNOWN;
			}
		}
		log.debug("poll complated");
		inputFiles = null;
		file = null;
		context.clearDirtyFlag();
		return FlowExecutionStatus.COMPLETED;
	}
}
