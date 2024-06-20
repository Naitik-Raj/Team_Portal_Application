package in.naitik.service;

import in.naitik.binding.LoginForm;
import in.naitik.binding.SignUpForm;
import in.naitik.binding.UnlockForm;

public interface UserService {
	public String login(LoginForm form);
	public boolean signUp(SignUpForm form);
	public boolean unlockAccount(UnlockForm form);
	public boolean forgotPwd(String email);
}
