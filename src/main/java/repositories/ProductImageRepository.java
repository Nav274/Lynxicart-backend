package repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import entities.Product;
import entities.Productimage;

public interface ProductImageRepository extends JpaRepository<Productimage, Integer> {

	@Query("Select pi.imageurl from Productimage pi where pi.product = :product")
	Optional<String> findByproduct_productid(Product product);
	
	@Query("Select pi from Productimage pi where pi.product = :product")
	Optional<Productimage> findByproduct(Product product);
}
