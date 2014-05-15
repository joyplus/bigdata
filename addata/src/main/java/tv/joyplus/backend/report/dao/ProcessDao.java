package tv.joyplus.backend.report.dao;

import java.util.List;

import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;

public interface ProcessDao {
	public List<JobResultDto> queryData(ParameterDto parameterDto);
}