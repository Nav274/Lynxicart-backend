package services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import entities.Otpauth;
import entities.User;
import repositories.OtpauthRepository;

@Service
public class OtpGeneration {
	
	@Autowired
	private OtpauthRepository otpauthrepository;
	
	@Autowired
	JavaMailSender mailsender;
	
	public boolean generateOtp(User userid) {
		
		String otp = String.format("%06d", new Random().nextInt(999999));
		
		otpauthrepository.deleteAllByUser(userid);
		 
		Otpauth otptoken = new Otpauth();
		otptoken.setUserid(userid);
		otptoken.setOtp(otp);
		otpauthrepository.save(otptoken);
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(userid.getEmail());
		msg.setSubject("This is your OTP to Change password at lynxi cart");
		msg.setText("This is a auto-generated email do not reply to this mail.");
		msg.setText("Your Otp is:"+ otp);
		mailsender.send(msg);
		
		
		return true;
	}

}
