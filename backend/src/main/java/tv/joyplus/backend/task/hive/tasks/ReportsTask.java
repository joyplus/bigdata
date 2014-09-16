package tv.joyplus.backend.task.hive.tasks;

import java.util.List;

import tv.joyplus.backend.task.hive.Task;
import tv.joyplus.backend.task.hive.model.Reports;

public abstract class ReportsTask implements Task{

	public abstract List<Reports> getReports(String date);
	public abstract void saveReports(List<Reports> reports);
	
	public void handle(String date) {
		// TODO Auto-generated method stub
		saveReports(getReports(date));
	}
}
