package in.naitik.service;

import java.util.List;

import in.naitik.binding.DashboardResponse;
import in.naitik.binding.EnqSearchCriteria;
import in.naitik.binding.EnquiryForm;
import in.naitik.entity.StudentEnqEntity;

public interface EnqService {
	
	public List<String> getCourses();
	public List<String> getEnqStatus();
	public EnquiryForm getEnquiry(Integer enqId);
	public DashboardResponse getDashboardData(Integer userId);
	public boolean saveEnquiry(EnquiryForm form);
	public List<StudentEnqEntity> getEnquiries();
	public List<StudentEnqEntity> getFilteredEnquiries(EnqSearchCriteria criteria, Integer userId);
}
