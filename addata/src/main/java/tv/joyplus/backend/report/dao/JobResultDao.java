package tv.joyplus.backend.report.dao;

import java.util.List;

import tv.joyplus.backend.report.dto.JobResultDto;

public interface JobResultDao {
	public void saveJobResults (List<JobResultDto> results);
	/**
	 * 
	 * @param reportId
	 * @param status 0,1,2;  (0 --> 正在生成， 1 --->生成成功， 2---->生成失败)
	 */
	public void updateReportStatus (String reportId, int status);
}




