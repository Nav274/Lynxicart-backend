package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "userdetails")
public class Userdetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "details_id")
	Integer detailsid;

	@JoinColumn(name = "user_id")
	@OneToOne
	User userid;
	
	@Column(name = "age")
	int age;
	
	String address;
	
	@Column(name = "contact_no")
	String contactno;
	

	public Userdetails() {
		super();
	}

	public Userdetails(User userid, int age, String address, String contactno) {
		super();
		this.userid = userid;
		this.age = age;
		this.address = address;
		this.contactno = contactno;
	}

	public Integer getDetailsid() {
		return detailsid;
	}

	public void setDetailsid(Integer detailsid) {
		this.detailsid = detailsid;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}
	
	
}
