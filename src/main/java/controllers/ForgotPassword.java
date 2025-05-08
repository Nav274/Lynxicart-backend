package controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entities.Otpauth;
import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import repositories.OtpauthRepository;
import repositories.TokenRepository;
import repositories.UserRepository;
import services.OtpGeneration;

@RestController
@CrossOrigin(origins="http://localhost:3000", allowCredentials="true")
@RequestMapping("/forgotpass")
public class ForgotPassword {
	
	@Autowired
	private UserRepository userrepository;
	
	@Autowired
	private OtpauthRepository otpauthrepository;
	
	@Autowired
	private OtpGeneration otpgeneration;
	
	@Autowired
	TokenRepository tokenrepository;
	
	BCryptPasswordEncoder passwordencoder = new BCryptPasswordEncoder();
	
//	@Autowired
//	private UserRepository userrepository;
	
	
	@GetMapping("/emailauth")
	public ResponseEntity<?> getAuthEmail(@RequestParam String email){
		
		try {
		
			User user = userrepository.findByEmail(email).orElseThrow(()-> new RuntimeException("user not found! register yourself"));
			
			int userid = user.getUser_id();
			
			otpgeneration.generateOtp(user);
			
			return ResponseEntity.ok(Map.of("otpresponse","Otp is sent to your e-mail id", "userid", userid));
		
		}
		
		catch(RuntimeException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/verifyotp")
	public ResponseEntity<?> verifyOtpauth(@RequestBody Map<String, Object> otp){
		
		try {
			
			User user = userrepository.findById(Integer.parseInt(otp.get("userid").toString())).orElseThrow(()->new IllegalArgumentException("user not found"));
			
			Optional<Otpauth> userotp = otpauthrepository.findByuseridANDotp(Integer.parseInt(otp.get("userid").toString()), otp.get("otp").toString());
			
			if(userotp.isPresent()) {
				
					 if (ChronoUnit.MINUTES.between(userotp.get().getCreatedat(), LocalDateTime.now()) > 5) { 
						 
						 	throw new IllegalArgumentException("Otp expired generate new one");

					 }
					 
					 return ResponseEntity.ok(Map.of("message","Verified successfull change password now!","userid", user.getUser_id()));
					 
				 }
			
					return ResponseEntity.badRequest().body("wrong otp generate new otp");
					
			}
		
		catch(IllegalArgumentException e) {
			
			 return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PostMapping("/changepass")
	public ResponseEntity<?> Changepassword(@RequestBody Map<String, Object> password){
		
		try {
			
			System.err.println(password.get("userid"));
			System.err.println(password.get("newpassword"));
			
			User user = userrepository.findById(Integer.parseInt(password.get("userid").toString())).orElseThrow(()->new IllegalArgumentException("user not found"));
			
			if(password.get("newpassword")==null) {
				
				throw new RuntimeException("enter the password");
			}
			
			String passwordhash = passwordencoder.encode(password.get("newpassword").toString());
			
			user.setPasswordhash(passwordhash);
			
			user.setUpdated_at(LocalDateTime.now());
			
			tokenrepository.deleteByUserid(user.getUser_id());
			
			userrepository.save(user);
			
			return ResponseEntity.accepted().body("password changed successfully");
		}
		
		catch(IllegalArgumentException e) {
			
			 return ResponseEntity.badRequest().body(e.getMessage());
			 
		}
	}

}
