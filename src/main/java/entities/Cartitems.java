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
@Table(name = "cartitems")
public class Cartitems {

	@Id
	@Column(name = "cart_item_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer cartitemid;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	Cart cartid;

	@OneToOne
	@JoinColumn(name = "product_id")
	Product productid;

	@Column(name = "quantity")
	Integer quantity;

	@Column(name = "price")
	BigDecimal price;

	@Column(name = "totalprice")
	BigDecimal totalprice;

	public Integer getCartitemid() {
		return cartitemid;
	}

	public void setCartitemid(Integer cartitemid) {
		this.cartitemid = cartitemid;
	}

	public Cart getCartid() {
		return cartid;
	}

	public void setCartid(Cart cartid) {
		this.cartid = cartid;
	}

	public Product getProductid() {
		return productid;
	}

	public void setProductid(Product productid) {
		this.productid = productid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Cartitems() {

	}

	public Cartitems(Cart cartid, Product productid, int totalquantity, BigDecimal price) {

		this.cartid = cartid;
		this.productid = productid;
		this.quantity = totalquantity;
		this.price = price;

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

}
