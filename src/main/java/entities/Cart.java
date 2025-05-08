package entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {

	@Id
	@Column(name = "cart_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartid;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User userid;

	@Column(name = "created_at", insertable=false, updatable=false)
	private LocalDateTime createdat;

	@OneToMany(mappedBy = "cartid", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Cartitems> cartitems;

	public Integer getCartid() {
		return cartid;
	}

	public void setCartid(Integer cartid) {
		this.cartid = cartid;
	}

	public User getUserid() {
		return userid;
	}

	public List<Cartitems> getCartitems() {
		return cartitems;
	}

	public void setCartitems(List<Cartitems> cartitems) {
		this.cartitems = cartitems;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public LocalDateTime getCreatedat() {
		return createdat;
	}

	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}

	public Cart() {

	}

}
