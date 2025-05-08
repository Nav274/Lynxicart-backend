package adminservices;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import entities.Category;
import entities.Product;
import entities.Productimage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import repositories.CategoryRepository;
import repositories.ProductImageRepository;
import repositories.ProductRepository;

@Service
public class AdminProductService {
	
	private final ProductRepository productrepository;
	private final ProductImageRepository productimagerepository;
	private final CategoryRepository categoryrepository;
	
	@PersistenceContext
	private EntityManager entityManager;

	public AdminProductService(ProductRepository productrepository, ProductImageRepository productimagerepository, CategoryRepository categoryrepository) {
		
		this.productrepository = productrepository;
		this.productimagerepository = productimagerepository;
		this.categoryrepository = categoryrepository;
		
	}
	
	public Productimage addProduct(Map<String,Object> productinfo){
	
		String productname = (String) productinfo.get("name");
		String productdesc = (String) productinfo.get("description");
		BigDecimal productprice = new BigDecimal(productinfo.get("price").toString());
		Integer productstock = Integer.parseInt(productinfo.get("stock").toString());
		
		Category category = categoryrepository.findBycategoryname(productinfo.get("category").toString()).orElseThrow(()->new RuntimeException("category not found"));
		
		String imageurl = (String) productinfo.get("imageurl");
		LocalDateTime updatedat = LocalDateTime.now();
		
	
		Product newProduct = new Product(productname, productdesc, productprice, productstock, category, updatedat);
		
		Product savedproduct = productrepository.save(newProduct);
		
		entityManager.refresh(savedproduct);
		
		Productimage newproductimage = new Productimage();
		
		newproductimage.setProduct(savedproduct);
		newproductimage.setImageurl(imageurl);
		
		Productimage savedproductimage = productimagerepository.save(newproductimage);
		
		return savedproductimage;
	
	}
	
	public Boolean modifyProduct(Map<String, Object> productinfo, int id){
		
		Product product = productrepository.findById(id).orElseThrow(()->new IllegalArgumentException("product not found"));
		
		if(!(productinfo.get("name") == null) && !productinfo.get("name").toString().isBlank()) {
			
			product.setName((String) productinfo.get("name"));
		}
		
		if(!(productinfo.get("description") == null) && !productinfo.get("description").toString().isBlank()) {
			
			product.setDescription((String) productinfo.get("description"));
		}
		
		if(!(productinfo.get("price") == null) && !productinfo.get("price").toString().isBlank()) {
			
			product.setPrice( new BigDecimal(productinfo.get("price").toString()));
			
		}
		
		if(!(productinfo.get("stock") == null) && !productinfo.get("stock").toString().isBlank()) {
			
			product.setStock(Integer.parseInt(productinfo.get("stock").toString()));
		}
		
		if(!(productinfo.get("category") == null) && !productinfo.get("category").toString().isBlank()) {
			
			Category category = categoryrepository.findBycategoryname(productinfo.get("category").toString()).orElseThrow(()->new RuntimeException("Category not found"));
			
			product.setCategory(category);
		}

			product.setUpdatedat(LocalDateTime.now());
		
			Product productent = productrepository.save(product);
			 
		Optional<Productimage> productimage = productimagerepository.findByproduct(product);
		
		if(productimage.isPresent()){
			
			if(!(productinfo.get("imageurl") == null) && !productinfo.get("imageurl").toString().isBlank()) {
				
				productimage.get().setImageurl((String)productinfo.get("imageurl"));
				
				productimagerepository.save(productimage.get());
				
				return true;
			}
			
		}
		
		else {
			
			if(!(productinfo.get("imageurl") == null) && !productinfo.get("imageurl").toString().isBlank()) {
				
				Productimage newproductimage = new Productimage();
				
				newproductimage.setImageurl((String)productinfo.get("imageurl"));
				newproductimage.setProduct(productent);
				
				productimagerepository.save(newproductimage);
				
				return true;
	
			}
			
			return false;
			
		}	
		
		return false;
		
	}
	
	
	public void deleteProduct(int productid) {
		
		Product product = productrepository.findById(productid).orElseThrow(()->new IllegalArgumentException("product not found"));
		
		productrepository.deleteById(productid);
	}
	

	public Map<String, Object> viewProduct(Integer productid) {
		
		Product product = productrepository.findById(productid).orElseThrow(()->new IllegalArgumentException("product not found"));
		
		String image = productimagerepository.findByproduct_productid(product).orElse("default image");
		
		Map<String, Object> productdetails = new HashMap<>();
		
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		
			productdetails.put("productimage", image);
			productdetails.put("productid", product.getProductid());
			productdetails.put("productname", product.getName());
			productdetails.put("productdesc", product.getDescription());
			productdetails.put("productprice", product.getPrice());
			productdetails.put("productstock", product.getStock());
			productdetails.put("productcategory", product.getCategory().getName());
			productdetails.put("created_at", product.getCreatedat().format(formatter));
			productdetails.put("updated_at", product.getUpdatedat().format(formatter));
		
		return productdetails;
		
	}

}