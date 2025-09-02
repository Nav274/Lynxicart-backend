package controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import adminservices.AdminUserService;
import entities.User;
import entities.Userdetails;
import enums.Role;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.UserdetailsRepository;
import services.LoginService;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GetUser {
	
	@Autowired
	LoginService loginservice;
	
	@Autowired
	UserdetailsRepository userdetailsrepository;
	
	@Autowired
	AdminUserService adminuserservice;
	
	@GetMapping("/get")
	public Map<String, Object> getUser(HttpServletRequest request){
		
		User user = (User) request.getAttribute("authenticatedUser");
		
		Map<String, Object> userdetails =adminuserservice.viewUserdetails(user.getEmail());
		
		return userdetails;
		
	}
	
//	@PutMapping("/modify/{userid}")
//	public ResponseEntity<?> modifyUserDetails(@RequestBody Map<String, Object> userinfo, @PathVariable int userid, HttpServletRequest request){
//		
//		try {
//			
//			User user = (User) request.getAttribute("authenticatedUser");
//			
//			if(user == null) {
//				
//				throw new IllegalArgumentException("Unauthorized");
//			}
//			
//			Boolean isupdated = adminuserservice.modifyUserdetails(userid, userinfo);
//			
//			if(isupdated == true) {
//			
//				return ResponseEntity.ok("User details updated successfully");
//			
//			}
//			
//			else {
//				
//				throw new RuntimeException();
//				
//			}
//		
//		}
//		
//		catch(RuntimeException e) {
//			
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		
//	}
//	
//	

	
	@DeleteMapping("/logout")
	public ResponseEntity<Map<String,String>> userLogout(HttpServletRequest request, HttpServletResponse response){
		
		User user = (User) request.getAttribute("authenticatedUser");
		
		try {
			
		if(user == null) {
			
			throw new IllegalArgumentException();
			
		}
		
		loginservice.logout(user);
		
		Cookie logoutcookie = new Cookie("authtoken",null);
		logoutcookie.setHttpOnly(true);
		logoutcookie.setMaxAge(0);
		logoutcookie.setPath("/");
		
		response.addCookie(logoutcookie);
		
		Map<String,String> logoutresponse = new HashMap<>();
		logoutresponse.put("message", "Logout successfull");
		
		return ResponseEntity.ok(logoutresponse);
		
		}
		
		catch(Exception e) {
			
			Map<String,String> logoutresponse = new HashMap<>();
			logoutresponse.put("message", "Logout failed,Try again");
			
			return ResponseEntity.badRequest().body(logoutresponse);
				
		}
		
	}
}
