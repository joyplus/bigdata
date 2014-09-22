package tv.joyplus.backend.task.hive.tasks;

import java.util.List;

import tv.joyplus.backend.task.hive.Task;
import tv.joyplus.backend.task.hive.model.FrequencyReport;

public abstract class FrequencyTask implements Task {

	public abstract String getAdIdSet(String date);
	public abstract List<FrequencyReport> getReports(String ads, String date);
	public abstract void saveReports(List<FrequencyReport> reports);
	
	@Override
	public void handle(String date) {
		// TODO Auto-generated method stub
		//获取ad_date=date 时的creative集合
		String adset = getAdIdSet(date);
		//generate reports
		generateFrequencyReports(adset,date);
	}
	private void generateFrequencyReports(String adset,String date){
		if(date ==null || date.trim().length()==0){
			return;
		}
		saveReports(getReports(adset,date));
	}
}
