package services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import controllers.LoginRequest;
import entities.Cart;
import entities.Token;
import entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import repositories.CartRepository;
import repositories.TokenRepository;
import repositories.UserRepository;

@Service
public class LoginService {

	
	private final Key SIGNING_KEY;
	private final BCryptPasswordEncoder passwordencoder;
	private final UserRepository userrepository;
	private final TokenRepository tokenrepository;
	private final CartRepository cartrepository;


	public LoginService(UserRepository userRepository, TokenRepository tokenrepository, CartRepository cartrepository,  @Value("${jwt_secret}") String jwtSecret) {

		this.userrepository = userRepository;
		this.tokenrepository = tokenrepository;
		this.cartrepository = cartrepository;
		this.passwordencoder = new BCryptPasswordEncoder();

		if (jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
			throw new IllegalArgumentException(
					"JWT_SECRET in application.properties must be at least 64 bytes long for HS512.");
		}

		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public User authenticate(String username, String password) {
		User user = userrepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (!passwordencoder.matches(password, user.getPasswordhash())) {
			throw new RuntimeException("Invalid username or password");
		}
		return user;
	}

	public String registerUser(User user) {

		
		if (userrepository.findByUsername(user.getUsername()).isPresent()){
					
			
			throw new RuntimeException("Username already taken");
					
		}
				
		if (userrepository.findByEmail(user.getEmail()).isPresent()) {
			
			throw new RuntimeException("Email already exists. Try logging in.");
					
		}
				
		user.setPasswordhash(passwordencoder.encode(user.getPasswordhash()));
				
		User registereduser = userrepository.save(user);
				 
		if(registereduser == null) {
					 
			throw new RuntimeException("unexpected error occurred");
			
		}
				
			Cart cart = new Cart();
			cart.setUserid(registereduser);
				
			cartrepository.save(cart);
				
			return "User registered Successfully! Login now";

		}
	

	public Optional<User> registeredUser(LoginRequest user) {
		

		Optional<User> userent = Optional.ofNullable(userrepository.findByUsername(user.getUsername()).orElseThrow(() -> new RuntimeException("Check username and password")));
		
		
		if (userent.isPresent() && passwordencoder.matches(user.getPassword(), userent.get().getPasswordhash())) {
			
			return userent;
			
		} else {
			
			throw new RuntimeException("Check username and password");
		}
	}

	public String generateToken(User user) {

		String token;
		
		LocalDateTime now = LocalDateTime.now();
		
		Optional<Token> existingToken = tokenrepository.findByuser_userid(user.getUser_id());

		if (existingToken.isPresent() && now.isBefore(existingToken.get().getExpires_at())) {
			
			token = existingToken.get().getJwttoken();
			
		} else {

			token = generateNewToken(user);
			existingToken.ifPresent(tokenValue -> tokenrepository.delete(existingToken.get()));

			saveToken(user, token);
		}
		return token;
	}

	private String generateNewToken(User user) {
		return Jwts.builder().setSubject(user.getUsername()).claim("role", user.getRole().name())
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
				.signWith(SIGNING_KEY, SignatureAlgorithm.HS512).compact();
	}

	public void saveToken(User user, String token) {
		Token jwtToken = new Token(user, token, LocalDateTime.now().plusHours(1));
		tokenrepository.save(jwtToken);
	}
	
	public void deleteToken(User user) {
		
		
	}
		
		
	

	public void logout(User user) {
		tokenrepository.deleteByUserid(user.getUser_id());
	}

	public boolean validateToken(String token) {
		
		try {

			System.err.println("VALIDATING TOKEN...");

			Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);

			Optional<Token> jwtToken = tokenrepository.findByjwttoken(token);
			if (jwtToken.isPresent()) {
				System.err.println("Token Expiry: " + jwtToken.get().getExpires_at());
				System.err.println("Current Time: " + LocalDateTime.now());
				return jwtToken.get().getExpires_at().isAfter(LocalDateTime.now());
			}
			return false;
			
		} catch (Exception e) {
			
			System.err.println("Token validation failed: " + e.getMessage());
			return false;
		}
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token).getBody().getSubject();
	}
}
