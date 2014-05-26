package tv.joyplus.backend.report.dao;

import java.util.List;

import tv.joyplus.backend.report.dto.JobResultDto;

public interface JobResultDao {
	public void saveJobResults (List<JobResultDto> results);
	
	public void updateReportStatus (String reportId);
}




