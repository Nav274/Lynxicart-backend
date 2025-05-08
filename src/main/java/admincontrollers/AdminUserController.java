package admincontrollers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adminservices.AdminProductService;
import adminservices.AdminUserService;
import entities.Product;
import entities.User;
import entities.Userdetails;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/admin/user")
public class AdminUserController {
	
	private final AdminUserService adminuserservice;
	
	public AdminUserController(AdminUserService adminuserservice) {
		
		this.adminuserservice = adminuserservice;
		
	}
	
		@PostMapping("/add")
		public ResponseEntity<?> addUserDetails(@RequestBody Map<String,Object> userinfo, HttpServletRequest request) {
		
			try {
				
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				
				throw new IllegalArgumentException("unauthorized");
			}
			
				Userdetails addeduser = adminuserservice.addUser(userinfo);
			
				return ResponseEntity.ok(addeduser);
			
			}
			
			catch(RuntimeException e) {
				
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
	
	
		@PutMapping("/modify/{userid}")
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
		
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<?> deleteUser(@PathVariable Integer id, HttpServletRequest request){
			
			try {
				
				User user = (User) request.getAttribute("authenticatedUser");
				
				if(user == null) {
					
					throw new IllegalArgumentException("unauthorized");
				}
				
				adminuserservice.deleteUser(id);
				
				return ResponseEntity.ok("user deleted successfully");
				
				}
				
				catch(RuntimeException e) {
					
					return ResponseEntity.badRequest().body(e.getMessage());
				}
				
			}
		
		@GetMapping("/get")
		public ResponseEntity<?> viewUserDetails(@RequestParam String emailid, HttpServletRequest request){
			
			try {
				
				User user = (User) request.getAttribute("authenticatedUser");
				
				if(user == null) {
					
					throw new IllegalArgumentException("unauthorized");
				}
				
					Object userdetails = adminuserservice.viewUserdetails(emailid);
					
					return ResponseEntity.ok(userdetails);
				
				}
				
				catch(RuntimeException e) {
					
					return ResponseEntity.badRequest().body(e.getMessage());
					
				}
				
			}
			
}
	

