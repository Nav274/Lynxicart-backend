package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartContoller {

	@Autowired
	CartService cartservice;

	@GetMapping("/items/count")
	public Map<String,Object> countitems( HttpServletRequest request) {
 
		Map<String,Object> response = new HashMap<>();
		
		User user  = (User) request.getAttribute("authenticatedUser");
		int itemcount = cartservice.getCountItems(user.getUser_id());
		
		response.put("count",itemcount);

		return response;
		
	}

	@GetMapping("/items")
	public ResponseEntity<?> fetchCartItems(HttpServletRequest request) {

		try {
			
			User user  = (User) request.getAttribute("authenticatedUser");
			List<Map<String, Object>> response = cartservice.getUserCartItems(user.getUser_id());

			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/item/add")
	public ResponseEntity<?> addNewCartItem(@RequestBody Map<String, Integer> item, HttpServletRequest request) {
		
		try {
			
		User user = (User) request.getAttribute("authenticatedUser");
		
		int userid = user.getUser_id();
		int productid = (int) item.get("productid");
		int quantity = (int) item.get("quantity");
		
		
		cartservice.addNewitem(userid, productid);

		return ResponseEntity.status(HttpServletResponse.SC_CREATED)
				.body(Map.of("message", "Added to cart successfully!","count", quantity));
		
		}catch(Exception error) {
			
			return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
					.body(Map.of("message", "added failed"));
			
		}
	}
	
	@PutMapping("/item/update")
	public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Integer> item, HttpServletRequest request) {
		
		try {
			
		User user = (User) request.getAttribute("authenticatedUser");
		
		int userid = user.getUser_id();
		int productid = (int) item.get("productid");
		int quantity = (int) item.get("quantity");
		
		cartservice.updateItem(userid, productid, quantity);
		
		return ResponseEntity.ok(Map.of("message", "updated successfully", "count", quantity));
		
		}catch(Exception error) {
			
			return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
					.body(Map.of("message", "update failed"));
			
		}
	}

	@DeleteMapping("/item/delete")
	public ResponseEntity<?> deleteCartItem(@RequestBody Map<String, Object> item, HttpServletRequest request) {

		try {

			User user = (User) request.getAttribute("authenticateduser");

			int userid = 11;

			int productid = (int) item.get("productid");

			cartservice.deleteItem(userid, productid);

			return ResponseEntity.status(HttpServletResponse.SC_ACCEPTED)
					.body(Map.of("success", "item deleted successfully"));

		}

		catch (NoSuchElementException | IllegalArgumentException e) {

			return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(Map.of("error", e.getMessage()));
		}
	}

}
