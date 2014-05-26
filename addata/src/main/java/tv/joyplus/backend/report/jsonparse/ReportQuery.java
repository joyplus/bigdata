package tv.joyplus.backend.report.jsonparse;

public class ReportQuery {
	private String type;
	private String dataCycle;
	private int frequency;
	private String[] dataResource; 
	private String[] dateRange; 
	private String[] items;
	private String[] groupBy;
	private String[] orderBy;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataCycle() {
		return dataCycle;
	}
	public void setDataCycle(String dataCycle) {
		this.dataCycle = dataCycle;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String[] getDataResource() {
		return dataResource;
	}
	public void setDataResource(String[] dataResource) {
		this.dataResource = dataResource;
	}
	public String[] getDateRange() {
		return dateRange;
	}
	public void setDateRange(String[] dateRange) {
		this.dateRange = dateRange;
	}
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	public String[] getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String[] groupBy) {
		this.groupBy = groupBy;
	}
	public String[] getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	} 
}
