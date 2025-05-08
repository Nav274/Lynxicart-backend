package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Cart;
import entities.Cartitems;
import entities.Product;
import entities.Productimage;
import repositories.CartRepository;
import repositories.CartitemRepository;
import repositories.ProductImageRepository;
import repositories.ProductRepository;
import repositories.UserRepository;

@Service
public class CartService {

	@Autowired
	UserRepository userrepository;

	@Autowired
	CartRepository cartrepository;

	@Autowired
	CartitemRepository cartitemrepository;

	@Autowired
	ProductRepository productrepository;

	@Autowired
	ProductImageRepository productimagerepository;

	public int getCountItems(int userid) {

		Cart cartent = cartrepository.findByuserid_userid(userid);

		int cartid = cartent.getCartid();

		int countitems = cartitemrepository.countcartitems(cartid);

		return countitems;
	}

	public List<Map<String, Object>> getUserCartItems(int userid) {

		List<Map<String, Object>> listcartitems = new ArrayList<>();

		Cart cartent = cartrepository.findByuserid_userid(userid);

		int cartid = cartent.getCartid();

		List<Cartitems> cartitems = cartitemrepository.findBycartid(cartid);

		if (cartitems == null) {

			throw new RuntimeException("No items found");
		}

		else {
			
			BigDecimal subtotal = BigDecimal.ZERO;

			for (Cartitems cart : cartitems) {

				Optional<String> image = productimagerepository
						.findByproduct_productid(cart.getProductid());

				Map<String, Object> cartitem = new HashMap<>();

				cartitem.put("productid", cart.getProductid().getProductid());
				cartitem.put("productname", cart.getProductid().getName());
				cartitem.put("productdesc", cart.getProductid().getDescription());
				cartitem.put("productprice", cart.getProductid().getPrice());
				cartitem.put("productimage", image.get());
				cartitem.put("productquantity", cart.getQuantity());
				
				listcartitems.add(cartitem);
				
				subtotal = subtotal.add(cart.getTotalprice());

			}

			return listcartitems;
		}

	}

	public void addNewitem(int userid, int productid)  {
		
		Cart cartentity = cartrepository.findByuserid_userid(userid);

		int cartid = cartentity.getCartid();

		Optional<Cart> cart = cartrepository.findById(cartid);

		Optional<Product> product = productrepository.findById(productid);

		Cartitems newitem = new Cartitems(cart.get(), product.get(), 1, product.get().getPrice());
		
		newitem.setPrice(product.get().getPrice());
		
		newitem.setTotalprice(product.get().getPrice().multiply(BigDecimal.valueOf(newitem.getQuantity())));

		cartitemrepository.save(newitem);

	}
	
	public void updateItem(int userid, int productid, int newquantity)  {
		
		if(newquantity==0) {
			
			deleteItem(userid, productid);
			return;
		}
		
		Cart cartentity = cartrepository.findByuserid_userid(userid);

		int cartid = cartentity.getCartid();

		Optional<Cartitems> cartitem = cartitemrepository.findByCartidAndProductid(cartid, productid);
		
		cartitem.get().setQuantity(newquantity);
		
		Optional<Product> product = productrepository.findById(productid);
		
		cartitem.get().setTotalprice(product.get().getPrice().multiply(BigDecimal.valueOf(cartitem.get().getQuantity())));
		
		cartitemrepository.save(cartitem.get());

	}


	public void deleteItem(int userid, int productid) {

		Cart cart = cartrepository.findByuserid_userid(userid);
		int cartid = cart.getCartid();

		Optional<Cartitems> cartitem = cartitemrepository.findByCartidAndProductid(cartid, productid);

		if (cartitem.isPresent()) {

			cartitemrepository.delete(cartitem.get());
			
		} else {

			throw new IllegalArgumentException("No item found to delete");
		}
	}

}
