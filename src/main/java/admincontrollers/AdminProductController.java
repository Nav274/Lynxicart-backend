package admincontrollers;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import adminservices.AdminProductService;
import entities.Category;
import entities.Product;
import entities.Productimage;
import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import repositories.ProductRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/admin/product")
public class AdminProductController {
	
	private final AdminProductService productservice;
	

	public AdminProductController(AdminProductService productservice) {
		
		this.productservice = productservice;
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addProductDetails(@RequestBody Map<String,Object> productinfo, HttpServletRequest request) {
		
		try {
			
		User user = (User) request.getAttribute("authenticatedUser");
		
		if(user == null) {
			
			throw new IllegalArgumentException("unauthorized");
		}
		
		Productimage addedproduct = productservice.addProduct(productinfo);
		
			return ResponseEntity.ok(addedproduct);
		
		}
		
		catch(RuntimeException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("/modify/{productid}")
	public ResponseEntity<?> modifyProductDetails(@RequestBody Map<String, Object> productinfo, @PathVariable int productid, HttpServletRequest request){
		
		try {
			
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				
				throw new IllegalArgumentException("unauthorized");
			}
			
			boolean ismodifiedproduct = productservice.modifyProduct(productinfo, productid);
			
				return ResponseEntity.ok("product modified successfully");
			
			}
			
			catch(RuntimeException e) {
				
				System.err.println(e.getMessage());
				
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> modifyProductDetails(@PathVariable Integer id, HttpServletRequest request){
		
		try {
			
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				
				throw new IllegalArgumentException("unauthorized");
			}
			
				productservice.deleteProduct(id);
			
				return ResponseEntity.ok("Product deleted successfully with Id"+id);
			
			}
			
			catch(RuntimeException e) {
				
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
	
	@GetMapping("/view/{id}")
	public ResponseEntity<?> viewProductDetails(@PathVariable Integer id, HttpServletRequest request){
		
		try {
			
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				
				throw new IllegalArgumentException("unauthorized");
			}
			
				Map<String, Object> productdetails = productservice.viewProduct(id);
			
				return ResponseEntity.ok(productdetails);
			
			}
			
			catch(RuntimeException e) {
				
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}
	

}
	
		
		

		
		
	

