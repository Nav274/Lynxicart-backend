package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entities.Cart;
import entities.Cartitems;
import entities.Category;
import entities.Product;
import entities.Productimage;
import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import repositories.CartRepository;
import repositories.CartitemRepository;
import repositories.CategoryRepository;
import repositories.ProductImageRepository;
import repositories.ProductRepository;
import services.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class ProductController {

	@Autowired
	ProductService productservice;

	@Autowired
	ProductRepository productrepository;

	@Autowired
	ProductImageRepository productimagerepository;
	
	@Autowired
	CartRepository cartrepository;
	
	@Autowired
	CartitemRepository cartitemrepository;
	
	@Autowired
	CategoryRepository categoryrepository;

	@GetMapping("/products")
	public ResponseEntity<?> fetchProductsByCategory(@RequestParam(required = false) String category, HttpServletRequest request) {
		
		try {

		User user = (User) request.getAttribute("authenticatedUser");
		
		System.out.println(user.getUsername());
		
		int userid = user.getUser_id();
		
		Cart cart = cartrepository.findByuserid_userid(userid);
		
		int cartid = cart.getCartid();
		
		
		if (category == null) {

			List<Product> products = productrepository.findAll();
			
			List<Map<String, Object>> response = productservice.fetchProductsinfo(products,  cartid);
			
			return ResponseEntity.ok(response);
			
		}
		
		else {
			
			Category categoryent = categoryrepository.findBycategoryname(category).orElseThrow(()->new IllegalArgumentException("product category not found"));
			
			List<Product> products = productrepository.findBycategory_categoryid(categoryent.getCategoryid());
			
			List<Map<String, Object>> response = productservice.fetchProductsinfo(products,  cartid);
			
			return ResponseEntity.ok(response);
			
		}
					
		} catch(Exception e) {
			
			return  ResponseEntity.badRequest().body(e.getMessage()+ e.fillInStackTrace());
			
		}
	}

}
