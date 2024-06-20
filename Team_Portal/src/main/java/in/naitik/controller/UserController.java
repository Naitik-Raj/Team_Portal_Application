package in.naitik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.naitik.binding.LoginForm;
import in.naitik.binding.SignUpForm;
import in.naitik.binding.UnlockForm;
import in.naitik.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/signup")
	public String signUpPage(Model model) {
		model.addAttribute("user", new SignUpForm());
		return "signup";
	}

	@PostMapping("/signup")
	public String handleSignUp(@ModelAttribute("user") SignUpForm form, Model model) {
		boolean status = userService.signUp(form);
		if (status) {
			model.addAttribute("succMsg", "Account Created, Check your email...");
		} else {
			model.addAttribute("errMsg", "Use Unique Id!!!");
		}
		return "signup";
	}

	@GetMapping("/unlock")
	public String unlockPage(@RequestParam String email, Model model) {

		UnlockForm ufObj = new UnlockForm();
		ufObj.setEmail(email);

		model.addAttribute("unlock", ufObj);

		return "unlock";
	}

	@PostMapping("/unlock")
	public String unlockUserAccount(@ModelAttribute("unlock") UnlockForm uForm, Model model) {
//		System.out.println(uForm);
		if (uForm.getNewPwd().equals(uForm.getConfirmPwd())) {
			boolean status = userService.unlockAccount(uForm);

			if (status) {
				model.addAttribute("succMsg", "Account Unlocked Successfully...");
			} else {
				model.addAttribute("errMsg", "Given  temporary password is incorrect, Check your email!");
			}

		} else {
			model.addAttribute("errMsg", "New password and Confirm password must be same.");
		}
		return "unlock";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}
	
	@PostMapping("/login")
	public String handleLoginPage(@ModelAttribute("loginForm") LoginForm lForm, Model model) {
		String status = userService.login(lForm);
		if(status.contains("success")) {
			return "redirect:/dashboard";
		}
		
		model.addAttribute("errMsg", status);
		
		return "login";
	}

	@GetMapping("/forgot")
	public String forgotPwdPage() {
		return "forgotPwd";
	}
	
	@PostMapping("/forgotPwd")
	public String forgotPwd(@RequestParam("email") String email, Model model) {
		
		boolean status = userService.forgotPwd(email);
		
		if (status) {
			model.addAttribute("succMsg", "Password sent to your mail...");
		} else {
			model.addAttribute("errMsg", "Please enter correct email!");
		}
		return "forgotPwd";
	}
}
