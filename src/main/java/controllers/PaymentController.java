package controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import entities.OrderItem;
import entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.PaymentService;

@RestController
@RequestMapping("/api/cart/payment")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PaymentController {

	@Autowired
	PaymentService paymentservice;

	@PostMapping("/create")
	public ResponseEntity<String> createPayment(@RequestBody Map<String, Object> requestbody,
			HttpServletRequest request) {

		try {
			User user = (User) request.getAttribute("authenticatedUser");

			if (user == null) {
				return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("user not found");
			}
			
			BigDecimal totalpayable = new BigDecimal(requestbody.get("totalamount").toString());
			
			String razorpayorderid = paymentservice.createOrder(user, totalpayable);

			return ResponseEntity.ok(razorpayorderid);

		} catch (RazorpayException e) {
			
		    e.printStackTrace();
			return ResponseEntity.internalServerError().body("Error creating the payment" + e.getMessage());
			 
			
		} catch (Exception e) {

			return ResponseEntity.badRequest().body("Invalid request data" + e.getMessage());
		}

	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody Map<String, Object> requestbody,
			HttpServletRequest request) {

		try {

			User user = (User) request.getAttribute("authenticatedUser");
			

			if (user == null) {

				return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("user not found");
			}

			int userid = user.getUser_id();


			String razorpayorderid = (String) requestbody.get("razorpayOrderId");
			String razorpaymentid = (String) requestbody.get("razorpayPaymentId");
			String razorpaymentsignature = (String) requestbody.get("razorpaySignature");
			BigDecimal amountpaid = new BigDecimal(requestbody.get("amountpaid").toString());
			
			
			boolean isVerified = paymentservice.verifypayment(razorpayorderid, razorpaymentid, razorpaymentsignature,
					userid, amountpaid);

			if (isVerified) {

				return ResponseEntity.ok("Payment Successfull");
			} else {

				throw new RuntimeException("unknown error occurred");
			}

		} catch (RazorpayException | RuntimeException e) {

			return ResponseEntity.internalServerError().body("internal server error" + e.getMessage());

		}

	}

}
