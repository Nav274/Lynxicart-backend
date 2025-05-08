package adminservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import entities.Cart;
import entities.Order;
import entities.User;
import entities.Userdetails;
import enums.Role;
import repositories.CartRepository;
import repositories.CartitemRepository;
import repositories.OrderItemRepository;
import repositories.OrderRepository;
import repositories.OtpauthRepository;
import repositories.TokenRepository;
import repositories.UserRepository;
import repositories.UserdetailsRepository;

@Service
public class AdminUserService {
	
		@Autowired
		UserRepository userrepository;
		
		@Autowired
		UserdetailsRepository userdetailsrepository;
		
		@Autowired
		TokenRepository tokenrepository;
		
		@Autowired
		CartRepository cartrepository;
		
		@Autowired
		CartitemRepository cartitemrepository;
		
		@Autowired
		OrderRepository orderrepository;
		
		@Autowired
		OrderItemRepository orderitemrepository;
		
		@Autowired
		OtpauthRepository otpauthrepository;
		
		BCryptPasswordEncoder passwordencoder = new BCryptPasswordEncoder();

	public Userdetails addUser(Map<String, Object> userinfo) {
		
		String name = (String) userinfo.get("name");
		String email = (String) userinfo.get("email");
		Role role = Role.valueOf((String) userinfo.get("role"));
		String password = "user";
		Integer age = Integer.parseInt((String) userinfo.get("age"));
		String address = (String) userinfo.get("address");
		String contactno = (String) userinfo.get("contactno");
		
		if(userrepository.findByUsername(name).isPresent()) {
			
			throw new IllegalArgumentException("username already taken");
		}
		
		if(userrepository.findByEmail(email).isPresent() || userdetailsrepository.findBycontactno(contactno).isPresent()){
			
			throw new IllegalArgumentException("user already present");
		}
		
		String passwordhash = passwordencoder.encode(password);
		
		User user = new User(name, email, role, passwordhash, LocalDateTime.now(), LocalDateTime.now());
		
		Userdetails userdetails = new Userdetails(user, age, address, contactno);
		
		userrepository.save(user);
		
		userdetailsrepository.save(userdetails);
		
		return userdetails;
		
	}
	
	public void deleteUser(int userid) {
		
		User user = userrepository.findById(userid).orElseThrow(()->new IllegalArgumentException("user not found"));
		
		otpauthrepository.deleteAllByUser(user);
		
		Cart cart = cartrepository.findByuserid_userid(user.getUser_id());
		
		cartitemrepository.deleteByCartid(cart.getCartid());
		
		cartrepository.delete(cart);
		
		Optional<List<Order>> orders = orderrepository.findByuserid(user);
		
		if(orders.isPresent()) {
			
			for(Order order : orders.get()) {
				
				orderitemrepository.deleteAllByOrderid(order.getOrderid());
				
				orderrepository.delete(order);
			}
			
		}
		
		userrepository.delete(user);
		
		Optional<Userdetails> userdetails = userdetailsrepository.findByuserid(user);
		
		if(userdetails.isPresent()) {
			
			userdetailsrepository.deleteByuser(user);
		}
		
		return;
			
	}

	public Map<String, Object> viewUserdetails(String emailid) {
		
		User user = userrepository.findByEmail(emailid).orElseThrow(()-> new IllegalArgumentException("user details not found"));
		
		Userdetails userdetails = userdetailsrepository.findByuserid(user).orElse(null);
		
		Map<String, Object> response = new HashMap<>();
		
		DateTimeFormatter datatimeformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
		response.put("userid", user.getUser_id());
		response.put("username", user.getUsername());
		response.put("emailid", user.getEmail());
		response.put("role", user.getRole());
		response.put("created_at", user.getCreated_at().format(datatimeformatter));
		response.put("updated_at", user.getUpdated_at().format(datatimeformatter));
		response.put("age", userdetails != null ? userdetails.getAge() : null );
		response.put("address", userdetails != null ?userdetails.getAddress() : null);
		response.put("contactno", userdetails != null ?userdetails.getContactno() : null);
		
		return response;
		
	}
	
	public Boolean modifyUserdetails(int id, Map<String, Object> userinfo) {
		
		Optional<User> user = userrepository.findById(id);
		
		if(user.isPresent()) {
		
			if(!(userinfo.get("name") == null) && !userinfo.get("name").toString().isBlank()) {
			
				user.get().setUsername((String)userinfo.get("name"));
			}
			
			if(!(userinfo.get("email") == null) && !userinfo.get("email").toString().isBlank()) {
							
				user.get().setEmail((String)userinfo.get("email"));
			}
			
			if(!(userinfo.get("role") == null) && !userinfo.get("role").toString().isBlank()) {
				
				user.get().setRole(Role.valueOf((String)userinfo.get("role")));
			}
		
			user.get().setUpdated_at(LocalDateTime.now());
			
			tokenrepository.deleteByUserid(user.get().getUser_id());
			
			userrepository.save(user.get());	
		
		}
		
		else {
			
			throw new IllegalArgumentException("user not found");
			
		}
		
		Optional<Userdetails> userdetails = userdetailsrepository.findByuserid(user.get());
		
		if(userdetails.isPresent()) {
				
			if(!(userinfo.get("phone") == null) && !userinfo.get("phone").toString().isBlank()) {
				
				userdetails.get().setContactno((String)userinfo.get("phone"));
				
			}
			
			if(!(userinfo.get("address") == null) && !userinfo.get("address").toString().isBlank()) {
				
				userdetails.get().setAddress((String)userinfo.get("address"));
			}
			
			if(!(userinfo.get("age") == null) && !userinfo.get("age").toString().isBlank()) {			
				
				userdetails.get().setAge(Integer.parseInt(userinfo.get("age").toString()));
				
			}
			
			user.get().setUpdated_at(LocalDateTime.now());
			
			userdetailsrepository.save(userdetails.get());
			
			return true;
			
		}
		
		else {
			
			Userdetails newuserdetail = new Userdetails();
			 
			newuserdetail.setUserid(user.get());
			
			if(!(userinfo.get("age") == null) && !userinfo.get("age").toString().isBlank()) {
			
				newuserdetail.setAge(Integer.parseInt(userinfo.get("age").toString()));
			
			}
			
			if(!(userinfo.get("address") == null) && !userinfo.get("address").toString().isBlank()) {
				
				newuserdetail.setAddress((String)userinfo.get("address"));
			}
			
			if(!(userinfo.get("phone") == null) && !userinfo.get("phone").toString().isBlank()) {
				
				newuserdetail.setContactno((String)userinfo.get("phone"));
			}
			
			user.get().setUpdated_at(LocalDateTime.now());
			
			userdetailsrepository.save(newuserdetail);
			
			return true;
			
		}
		
		
	}

}
