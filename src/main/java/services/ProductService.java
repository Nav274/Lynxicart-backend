package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Cartitems;
import entities.Category;
import entities.Product;
import entities.Productimage;
import repositories.CartitemRepository;
import repositories.CategoryRepository;
import repositories.ProductImageRepository;
import repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productrepository;

	@Autowired
	CategoryRepository categoryrepository;

	@Autowired
	ProductImageRepository productimagerepository;
	
	@Autowired
	CartitemRepository cartitemrepository;

	
	public List<Map<String, Object>> fetchProductsinfo(List<Product> products, int cartid){
		
			List<Map<String, Object>> response = new ArrayList<>();	

			for (Product product : products) {

				Map<String, Object> productdetails = new HashMap<>();
				
				Optional<Cartitems> cartitem = cartitemrepository.findByCartidAndProductid(cartid, product.getProductid());

				productdetails.put("productid", product.getProductid());
				productdetails.put("product name", product.getName());
				productdetails.put("product stock", product.getStock());
				productdetails.put("product price", product.getPrice());
				productdetails.put("product description", product.getDescription());
				productdetails.put("cart quantity", cartitem.map(Cartitems::getQuantity).orElse(0));

				Optional<String> image = productimagerepository.findByproduct_productid(product);
						
				if (image.isPresent()) {

					String imageurl = image.get();

					productdetails.put("image", imageurl);
				}

				response.add(productdetails);
			}

			return response;
			
		}

		
	}
	


