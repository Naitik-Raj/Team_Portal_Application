package in.naitik.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.naitik.binding.DashboardResponse;
import in.naitik.binding.EnqSearchCriteria;
import in.naitik.binding.EnquiryForm;
import in.naitik.entity.CourseEntity;
import in.naitik.entity.EnqStatusEntity;
import in.naitik.entity.StudentEnqEntity;
import in.naitik.entity.UserDetailsEntity;
import in.naitik.repo.CourseRepo;
import in.naitik.repo.EnqStatusRepo;
import in.naitik.repo.StudentEnqRepo;
import in.naitik.repo.UserDetailsRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class EnqServiceImpl implements EnqService {

	@Autowired
	private UserDetailsRepo userDetailsRepo;
	
	@Autowired
	private CourseRepo courseRepo;
	
	@Autowired
	private EnqStatusRepo statusRepo;
	
	@Autowired
	private StudentEnqRepo enqRepo;
	
	@Autowired
	private HttpSession session;

	@Override
	public DashboardResponse getDashboardData(Integer userId) {
		
		DashboardResponse response = new DashboardResponse();
		
		Optional<UserDetailsEntity> findById = userDetailsRepo.findById(userId);
		
		if (findById.isPresent()) {
			UserDetailsEntity userEntity = findById.get();

			List<StudentEnqEntity> enquiries = userEntity.getEnquiries();

			Integer totalCnt = enquiries.size();

			Integer enrolledCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("ENROLLED"))
					.collect(Collectors.toList()).size();
			
			Integer lostCnt = enquiries.stream().filter(e -> e.getEnqStatus().equals("LOST"))
					.collect(Collectors.toList()).size();
			

			response.setTotalEnqCount(totalCnt);
			response.setEnrollmentCount(enrolledCnt);
			response.setLostCount(lostCnt);
			
		}

		return response;
	}

	@Override
	public List<String> getCourses() {
		List<CourseEntity>findAll = courseRepo.findAll();
		
		List<String> names = new ArrayList<>();
		for(CourseEntity entity: findAll) {
			names.add(entity.getCourseName());
		}
		return names;
	}

	@Override
	public List<String> getEnqStatus() {
		List<EnqStatusEntity>findAll = statusRepo.findAll();
		
		List<String> statusList = new ArrayList<>();
		for(EnqStatusEntity entity: findAll) {
			statusList.add(entity.getStatusName());
		}
		return statusList;
	}

	@Override
	public EnquiryForm getEnquiry(Integer enqId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveEnquiry(EnquiryForm form) {
		StudentEnqEntity entity = new StudentEnqEntity();
		BeanUtils.copyProperties(form, entity);
		Integer userId = (Integer) session.getAttribute("userId");
		
		UserDetailsEntity uEntity = userDetailsRepo.findById(userId).get();
		entity.setUser(uEntity);
		enqRepo.save(entity);
		
		return true;
	}

	@Override
	public List<StudentEnqEntity> getEnquiries() {
		Integer userId = (Integer)session.getAttribute("userId");
		Optional<UserDetailsEntity> findById = userDetailsRepo.findById(userId);
		if(findById.isPresent()) {
			UserDetailsEntity userDetailsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDetailsEntity.getEnquiries();
			return enquiries;
		}
		return null;
	}

	@Override
	public List<StudentEnqEntity> getFilteredEnquiries(EnqSearchCriteria criteria, Integer userId) {
		Optional<UserDetailsEntity> findById = userDetailsRepo.findById(userId);
		if(findById.isPresent()) {
			UserDetailsEntity userDetailsEntity = findById.get();
			List<StudentEnqEntity> enquiries = userDetailsEntity.getEnquiries();
			
			//filter logic
			if(null != criteria.getCourseName() & !"".equals(criteria.getCourseName())) {
				enquiries = enquiries.stream()
						.filter(e -> e.getCourseName().equals(criteria.getCourseName()))
						.collect(Collectors.toList());
			}
			if(null != criteria.getClassMode() & !"".equals(criteria.getClassMode())) {
				enquiries = enquiries.stream()
						.filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());
			}
			if(null != criteria.getEnqStatus() & !"".equals(criteria.getEnqStatus())) {
				enquiries = enquiries.stream()
						.filter(e -> e.getEnqStatus().equals(criteria.getEnqStatus()))
						.collect(Collectors.toList());
			}
			return enquiries;
		} 
		return null;
	}

}
