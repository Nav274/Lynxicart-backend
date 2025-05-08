package services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import entities.OrderItem;
import entities.Productimage;
import repositories.OrderItemRepository;
import repositories.OrderRepository;
import repositories.ProductImageRepository;

@Service
public class OrderService {
	
	private final OrderRepository orderrepository;
	private final OrderItemRepository orderitemrepository;
	private final ProductImageRepository productimagerepository;
	
	
	public OrderService(OrderRepository orderrepository, OrderItemRepository orderitemrepository,ProductImageRepository productimagerepository) {
		
		this.orderrepository = orderrepository;
		this.orderitemrepository = orderitemrepository;
		this.productimagerepository = productimagerepository;
		
	}

	public List<Map<String, Object>> fetchOrderitemsOfUser(int userid) {
	
		List<String> ordersids = orderrepository.fetchByuseridAndStatus(userid);
		
		List<Map<String,Object>> orderentity = new ArrayList<>();
		
		for(String orderid : ordersids) {
			
			List<OrderItem> orderitems = orderitemrepository.findByOrderid_orderid(orderid);
			
			for(OrderItem orderitem : orderitems ) {
				
				Optional<String> productimage = productimagerepository.findByproduct_productid(orderitem.getProductid());
				
				LocalDateTime orderdateandtime = orderrepository.fetchTimeByOrderid(orderid);
				
				Map<String,Object> orderdetails = new HashMap<>();
				
				String imageurl;
				
				if(productimage==null) {
					
					 imageurl = "default image";
					
				}
				else {
					
					 imageurl = productimage.get();
				
				}
				
				orderdetails.put("order-id",orderitem.getOrderid().getOrderid() );
				orderdetails.put("order-product-name",orderitem.getProductid().getName() );
				orderdetails.put("order-product-image",imageurl );
				orderdetails.put("order-product-quantity",orderitem.getQuantity() );
				orderdetails.put("order-product-price",orderitem.getPrice() );
				orderdetails.put("order-status","SUCCESS" );
				
				DateTimeFormatter datatimeformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				orderdetails.put("order-time",orderdateandtime.format(datatimeformatter));
				
				orderentity.add(orderdetails);
				
			}
			
		}
		
		return orderentity;
		
	}

}
