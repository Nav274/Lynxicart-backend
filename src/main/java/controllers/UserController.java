package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import adminservices.AdminUserService;
import entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.LoginService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class UserController {

	@Autowired
	private LoginService loginservice;
	
	@Autowired
	private AdminUserService adminuserservice;
	
	BCryptPasswordEncoder passwordencoder = new BCryptPasswordEncoder();

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		try {
			
			String registermessage = loginservice.registerUser(user);
			
			return ResponseEntity.ok(Map.of("message", registermessage ));

		} catch (RuntimeException e) {

			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> registeredUser(@RequestBody LoginRequest login, HttpServletResponse response) {

		try {
			
			Optional<User> userent = loginservice.registeredUser(login);

			String token = loginservice.generateToken(userent.get());

			Cookie cookie = new Cookie("authtoken", token);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			cookie.setSecure(false);
			response.addCookie(cookie);

			Map<String, Object> responsebody = new HashMap<>();

			responsebody.put("message", "Login Successfull");
			responsebody.put("role", userent.get().getRole());

			return ResponseEntity.ok(responsebody);

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(e.getMessage());

		}

	}
	
	@PutMapping("/usermodify/{userid}")
	public ResponseEntity<?> modifyUserDetails(@RequestBody Map<String, Object> userinfo, @PathVariable int userid, HttpServletRequest request){
		
		try {
			
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				
				throw new IllegalArgumentException("Unauthorized");
			}
			
			Boolean isupdated = adminuserservice.modifyUserdetails(userid, userinfo);
			
			if(isupdated == true) {
			
				return ResponseEntity.ok("User details updated successfully");
			
			}
			
			else {
				
				throw new RuntimeException();
				
			}
		
		}
		
		catch(RuntimeException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
}
