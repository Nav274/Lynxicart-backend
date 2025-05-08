package entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderitems")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private int orderitemid;

	@JoinColumn(name = "order_id")
	@ManyToOne
	private Order orderid;
	
	@OneToOne
	@JoinColumn(name = "product_id")
	private Product productid;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "totalprice")
	private BigDecimal totalprice;

	public int getOrderitemid() {
		return orderitemid;
	}

	public void setOrderitemid(int orderitemid) {
		this.orderitemid = orderitemid;
	}

	public OrderItem(Order orderid, Product productid, int quantity, BigDecimal price, BigDecimal totalprice) {
		super();

		this.orderid = orderid;
		this.productid = productid;
		this.quantity = quantity;
		this.price = price;
		this.totalprice = totalprice;
	}

	public Order getOrderid() {
		return orderid;
	}

	public void setOrderid(Order orderid) {
		this.orderid = orderid;
	}

	public Product getProductid() {
		return productid;
	}

	public void setProductid(Product productid) {
		this.productid = productid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public OrderItem() {
		super();
	}

}