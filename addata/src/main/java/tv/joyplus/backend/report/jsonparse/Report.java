package tv.joyplus.backend.report.jsonparse;

public class Report {

	private int report_id;
	private String queryJson;
	public int getReport_id() {
		return report_id;
	}
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}
	public String getQueryJson() {
		return queryJson;
	}
	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}
}
