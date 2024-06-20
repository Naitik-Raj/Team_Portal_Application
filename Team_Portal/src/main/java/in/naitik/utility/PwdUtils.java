package in.naitik.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class PwdUtils {

	public static String generateRandomPwd() {
		String characters ="abcdefghijklmnopqrstucqxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321";
		return RandomStringUtils.random(6, characters);
	}
}
