package tv.joyplus.backend.sample;

import java.util.List;

import tv.joyplus.backend.report.dao.ProcessDao;
import tv.joyplus.backend.report.dto.JobResultDto;
import tv.joyplus.backend.report.dto.ParameterDto;

public class ProcessDaoSampleImpl implements ProcessDao {




	public List<JobResultDto> queryData(ParameterDto parameterDto) {
		System.out.println("Thread " + Thread.currentThread().getName());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
