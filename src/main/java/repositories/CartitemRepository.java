package repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entities.Cartitems;
import jakarta.transaction.Transactional;

@Repository
public interface CartitemRepository extends JpaRepository<Cartitems, Integer> {

	// List<Cartitems> findBycartid_cartid(int cartid);

	@Query("Select c from Cartitems c where c.cartid.cartid = :cartid AND c.productid.productid = :productid")
	Optional<Cartitems> findByCartidAndProductid(int cartid, int productid);

	@Query("Select Coalesce(Sum(quantity), 0) from Cartitems where cartid.cartid=:cartid")
	int countcartitems(int cartid);

	@Query("Select c from Cartitems c where c.cartid.cartid=:cartid")
	List<Cartitems> findBycartid(int cartid);

	@Modifying
	@Transactional
	@Query("delete from Cartitems c where c.cartid.cartid=:cartid")
	void deleteByCartid(int cartid);

}
