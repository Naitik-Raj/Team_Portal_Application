package in.naitik.binding;

import lombok.Data;

@Data
public class DashboardResponse {
	
	private Integer totalEnqCount;
	private Integer enrollmentCount;
	private Integer lostCount;
	public Integer getTotalEnqCount() {
		return totalEnqCount;
	}
	public void setTotalEnqCount(Integer totalEnqCount) {
		this.totalEnqCount = totalEnqCount;
	}
	public Integer getEnrollmentCount() {
		return enrollmentCount;
	}
	public void setEnrollmentCount(Integer enrollmentCount) {
		this.enrollmentCount = enrollmentCount;
	}
	public Integer getLostCount() {
		return lostCount;
	}
	public void setLostCount(Integer lostCount) {
		this.lostCount = lostCount;
	}
	
}
