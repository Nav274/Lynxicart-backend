package services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import entities.Cart;
import entities.Cartitems;
import entities.Order;
import entities.OrderItem;
import entities.User;
import enums.OrderStatus;
import jakarta.transaction.Transactional;
import repositories.CartRepository;
import repositories.CartitemRepository;
import repositories.OrderItemRepository;
import repositories.OrderRepository;

@Service
public class PaymentService {

	@Value("${razorpay.key.id}")
	private String razor_pay_key_id;

	@Value("${razorpay.key.secret}")
	private String razor_pay_secret_key;

	private final OrderRepository orderrepository;

	private final OrderItemRepository orderitemrepository;

	private final CartRepository cartrepository;

	private final CartitemRepository cartitemrepository;

	public PaymentService(OrderRepository orderrepository, OrderItemRepository orderitemrepository,
			CartRepository cartrepository, CartitemRepository cartitemrepository) {

		this.cartrepository = cartrepository;
		this.cartitemrepository = cartitemrepository;
		this.orderrepository = orderrepository;
		this.orderitemrepository = orderitemrepository;

	}
	
	@Transactional
	public String createOrder(User user_id, BigDecimal totalpayable) throws RazorpayException {

		RazorpayClient rzp = new RazorpayClient(razor_pay_key_id, razor_pay_secret_key);
		
		JSONObject orderreq = new JSONObject();

		orderreq.put("amount",totalpayable.multiply(BigDecimal.valueOf(100)).intValue());
		orderreq.put("currency", "INR");
		orderreq.put("receipt", "txn" + System.currentTimeMillis());
		orderreq.put("payment_capture", 1);
		
		com.razorpay.Order rzporder = rzp.orders.create(orderreq);
		
		Order order = new Order();

		order.setOrderid(rzporder.get("id"));
		order.setCreatedat(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		order.setTotalamount(totalpayable);
		order.setShippingaddress("bengaluru");
		order.setUpdatedat(LocalDateTime.now());
		order.setUserid(user_id);
		order.setAmountpaid(null);
			
		Order item = orderrepository.save(order);

		return rzporder.get("id");
	}

	@Transactional
	public boolean verifypayment(String razorpayorderid, String razorpaymentid, String razorpaymentsignature,
			int userid, BigDecimal amountpaid) throws RazorpayException {

		JSONObject attributes = new JSONObject();

		attributes.put("razorpay_order_id", razorpayorderid);
		attributes.put("razorpay_signature", razorpaymentsignature);
		attributes.put("razorpay_payment_id", razorpaymentid);
		
		System.out.println("razor pay order-id:"+razorpayorderid);

		boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razor_pay_secret_key);

		if (isSignatureValid) {

			Order order = orderrepository.findById(razorpayorderid).orElseThrow(() -> new RuntimeException("Order not found"));
						
			order.setUpdatedat(LocalDateTime.now());
			order.setStatus(OrderStatus.SUCCESS);
			order.setAmountpaid(amountpaid);
			orderrepository.save(order);

			Cart cart = cartrepository.findByuserid_userid(userid);

			List<Cartitems> cartitems = cartitemrepository.findBycartid(cart.getCartid());

			for (Cartitems cartitem : cartitems) {

				OrderItem orderitem = new OrderItem(order, cartitem.getProductid(),
						cartitem.getQuantity(), cartitem.getPrice(), cartitem.getTotalprice());

				orderitemrepository.save(orderitem);
			}


			cartitemrepository.deleteByCartid(cart.getCartid());

			return true;

		} else {

			return false;
		}

	}

}
