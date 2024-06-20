package in.naitik.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import in.naitik.binding.DashboardResponse;
import in.naitik.binding.EnqSearchCriteria;
import in.naitik.binding.EnquiryForm;
import in.naitik.entity.StudentEnqEntity;
import in.naitik.service.EnqService;

@Controller
public class EnqController {

	@Autowired
	private EnqService enqService;

	@Autowired
	private HttpSession session;

	@GetMapping("/logout")
	public String logoutPage() {
		session.invalidate();
		return "index";
	}

	@GetMapping("/dashboard")
	public String dashboardPage(Model model) {
		Integer userId = (Integer) session.getAttribute("userId");
		DashboardResponse dashboardData = enqService.getDashboardData(userId);
		model.addAttribute("dashboardData", dashboardData);
		return "dashboard";
	}

	@GetMapping("/enquiry")
	public String addEnqPage(Model model) {
		// get courses for drop down
		initModel(model);

		return "add-enquiry";
	}

	@PostMapping("/addEnq")
	public String addEnquiry(@ModelAttribute("formObj") EnquiryForm formObj, Model model) {
		System.out.println(formObj);

		boolean status = enqService.saveEnquiry(formObj);

		if (status) {
			model.addAttribute("succMsg", "Enquiry Added.");
		} else {
			model.addAttribute("errMsg", "Problem Occured.");
		}
		return "add-enquiry";
	}

	@GetMapping("/enquiries")
	public String viewEnquiriesPage(Model model) {
		initModel(model);
//		model.addAttribute("searcForm", new EnqSearchCriteria());
		List<StudentEnqEntity> enquiries = enqService.getEnquiries();
		model.addAttribute("enquiries", enquiries);
		return "view-enquiries";
	}

	@GetMapping("/filter-enquiries")
	public String getFilteredEnq(@RequestParam String cName, @RequestParam String status, @RequestParam String mode,
			Model model) {
		EnqSearchCriteria criteria = new EnqSearchCriteria();
		criteria.setCourseName(cName);
		criteria.setClassMode(mode);
		criteria.setEnqStatus(status);
		Integer userId = (Integer) session.getAttribute("userId");
		List<StudentEnqEntity> filteredEnquiries = enqService.getFilteredEnquiries(criteria, userId);
		model.addAttribute("enquiries",filteredEnquiries);
		return "filter-enquiries-page";

	}

	private void initModel(Model model) {
		List<String> courses = enqService.getCourses();

		// get enq status for drop down
		List<String> enqStatus = enqService.getEnqStatus();

		// create binding class object
		EnquiryForm formObj = new EnquiryForm();

		// set data in model obj
		model.addAttribute("courseNames", courses);
		model.addAttribute("statusNames", enqStatus);
		model.addAttribute("formObj", formObj);

	}

}
