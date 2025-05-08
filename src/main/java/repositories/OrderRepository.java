package repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import entities.Order;
import entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	
	@Query("Select o.orderid from Order o where o.userid.userid = :userid AND o.status = 'SUCCESS'")
	List<String> fetchByuseridAndStatus(@Param("userid") int userid);

	@Query("Select o.updatedat from Order o where o.orderid=:orderid")
	LocalDateTime fetchTimeByOrderid(@Param("orderid") String orderid);
	
	Optional<List<Order>> findByuserid(User user);
} 
