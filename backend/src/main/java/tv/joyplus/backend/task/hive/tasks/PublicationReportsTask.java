package tv.joyplus.backend.task.hive.tasks;

import java.util.List;

import tv.joyplus.backend.task.hive.Task;
import tv.joyplus.backend.task.hive.model.PublicationReports;

public abstract class PublicationReportsTask implements Task{

	public abstract List<PublicationReports> getPublicationReports(String date);
	public abstract List<PublicationReports> getPublicationReportsWithCampaign(String date);
	public abstract void saveReports(List<PublicationReports> reports);
	
	public void handle(String date) {
		// TODO Auto-generated method stub
		generatePublicationReports(date);
		generatePublicationWithCampaign(date);
	}
	
	private void generatePublicationReports(String date){
		saveReports(getPublicationReports(date));
	}
	
	private void generatePublicationWithCampaign(String date){
		saveReports(getPublicationReportsWithCampaign(date));
	}
}
