package in.naitik.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

//	public JavaMailSender getMailSender() {
//		return mailSender;
//	}
//
//	public void setMailSender(JavaMailSender mailSender) {
//		this.mailSender = mailSender;
//	}

	public boolean sendEmail(String to, String sub, String body) {
		boolean isSent = false;

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			
			helper.setTo(to);
			helper.setSubject(sub);
			helper.setText(body, true);
			
			mailSender.send(mimeMessage);
			
			isSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSent;
	}
}
