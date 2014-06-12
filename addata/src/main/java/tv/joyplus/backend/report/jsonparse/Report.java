package tv.joyplus.backend.report.jsonparse;

public class Report {

	private int report_id;
	private String queryJson;
	private String timestamp;
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
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
