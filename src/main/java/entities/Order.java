package entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "order_id")
	private String orderid;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User userid;

	@Column(name = "total_amount")
	private BigDecimal totalamount;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "amount_paid")
	private BigDecimal amountpaid;

	@Column(name = "created_at")
	private LocalDateTime createdat;

	@Column(name = "updated_at")
	private LocalDateTime updatedat;
	
	@Column(name = "shipping_address")
	private String shippingaddress;

	public BigDecimal getAmountpaid() {
		return amountpaid;
	}

	public void setAmountpaid(BigDecimal amountpaid) {
		this.amountpaid = amountpaid;
	}

	public String getShippingaddress() {
		return shippingaddress;
	}

	public void setShippingaddress(String shippingaddress) {
		this.shippingaddress = shippingaddress;
	}

	@OneToMany(mappedBy = "orderid", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderitems;

	public List<OrderItem> getOrderitems() {
		return orderitems;
	}

	public void setOrderitems(List<OrderItem> orderitems) {
		this.orderitems = orderitems;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	public LocalDateTime getUpdatedat() {
		return updatedat;
	}

	public void setUpdatedat(LocalDateTime updatedat) {
		this.updatedat = updatedat;
	}

	public Order() {

	}

}
