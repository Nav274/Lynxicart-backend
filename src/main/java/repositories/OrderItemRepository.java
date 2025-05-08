package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import entities.OrderItem;
import jakarta.transaction.Transactional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	List<OrderItem> findByOrderid_orderid(String orderid);
	
	@Modifying
	@Transactional
	@Query("delete from OrderItem oi where oi.orderid.orderid = :orderid")
	void deleteAllByOrderid(String orderid);

}
