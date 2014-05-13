package tv.joyplus.backend.report.task;

import tv.joyplus.backend.report.dao.JobResult;

public interface ReportTask {

	public String getJob();
	public JobResult queryData(String strJobText);
	public boolean saveData(JobResult result);
}
