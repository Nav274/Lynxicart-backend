package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entities.Cart;
import entities.User;
import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByuserid_userid(Integer userid);

	@Modifying
	@Transactional
	@Query("delete from Cart c where c.userid = :user")
	void deleteByuser(User user);

}
