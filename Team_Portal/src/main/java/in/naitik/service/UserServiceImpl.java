package in.naitik.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.naitik.binding.LoginForm;
import in.naitik.binding.SignUpForm;
import in.naitik.binding.UnlockForm;
import in.naitik.entity.UserDetailsEntity;
import in.naitik.repo.UserDetailsRepo;
import in.naitik.utility.EmailUtils;
import in.naitik.utility.PwdUtils;
import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailsRepo userDtlsRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	//session is predefined in servlet api
	@Autowired
	private HttpSession session;

	@Override
	public String login(LoginForm form) {
		UserDetailsEntity entity = userDtlsRepo.findByEmailAndPwd(form.getEmail(), form.getPwd());
		if (entity == null) {
			return "Invalid Credentials.";
		}
		if (entity.getAccStatus().equals("LOCKED")) {
			return "Please unlock your account.";
		}
		//create session and store user data in session object
		session.setAttribute("userId", entity.getUserId());
		
		return "success";
	}

	@Override
	public boolean signUp(SignUpForm form) {

		UserDetailsEntity user = userDtlsRepo.findByEmail(form.getEmail());

		if (user != null) {
			return false;
		}

		// copy data from binding obj to entity obj
		UserDetailsEntity entity = new UserDetailsEntity();
		BeanUtils.copyProperties(form, entity);

		// generate pwd and set to object
		String tempPwd = PwdUtils.generateRandomPwd();
		entity.setPwd(tempPwd);

		// set a/c status as locked
		entity.setAccStatus("LOCKED");
		// insert record
		userDtlsRepo.save(entity);

		String to = form.getEmail();
		String sub = "Unlock Your Account";
		StringBuffer body = new StringBuffer("");
		body.append("<h1> Use below temporary password to unlock your account </h1>");
		body.append("Temporary Pwd: " + tempPwd);
		body.append("<br/>");
		body.append("<a href=\"http://localhost:8080/unlock?email=" + to + "\">Click Here to unlock your account</a>");
		// send email to unlock account
		emailUtils.sendEmail(to, sub, body.toString());

		return true;
	}

	@Override
	public boolean unlockAccount(UnlockForm form) {
		UserDetailsEntity entity = userDtlsRepo.findByEmail(form.getEmail());

		if (entity.getPwd().equals(form.getTempPwd())) {
			entity.setPwd(form.getNewPwd());
			entity.setAccStatus("UNLOCKED");
			userDtlsRepo.save(entity);

			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean forgotPwd(String email) {
		//check record present in db with given email
		UserDetailsEntity entity = userDtlsRepo.findByEmail(email);
		//if record is not available send false
		if(entity == null) {
			return false;
		}
		//if record available send pwd to email and send true
		String subject = "Recover Your Password";
		String body = "Your pwd is :: " + entity.getPwd();
		
		emailUtils.sendEmail(email, subject, body);
		return true;
	}

}
