package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findBycategory_categoryid(Integer categoryno);

}
