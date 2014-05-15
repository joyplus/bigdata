package tv.joyplus.backend.report.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;

public class ProcessDaoImpl extends JdbcDaoSupport implements ProcessDao {

	public List<JobResultDto> queryData(ParameterDto parameterDto) {
		// TODO Auto-generated method stub
		return null;
	}
}
