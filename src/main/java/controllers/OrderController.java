package controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import repositories.OrderRepository;
import services.OrderService;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins="http://localhost:3000",allowCredentials="true")
public class OrderController {
	
	private final OrderService orderservice;

	public OrderController(OrderService orderservice) {
		
		this.orderservice = orderservice;
		
		
	}
	
	@GetMapping("/items")
	public ResponseEntity<?> fetchOrderitems(HttpServletRequest request){
		
		try {
			
		User user = (User) request.getAttribute("authenticatedUser");
		
		Integer userid = user.getUser_id();
		
		List<Map<String,Object>> orderitemsresponse = orderservice.fetchOrderitemsOfUser(userid);
		
		return ResponseEntity.ok(orderitemsresponse);
				
		}
		catch(RuntimeException  e) {
			
			return ResponseEntity.internalServerError().body("message"+e.getMessage());
			
		}

			
	}
	

}
