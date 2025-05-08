package entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="otpauth")
public class Otpauth {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="otp_id")
	Integer otpid;
	
	@JoinColumn(name="user_id")
	@ManyToOne
	User userid;
	
	String otp;
	
	@Column(name="created_at", insertable=false, updatable=false)
	LocalDateTime createdat;

	public Otpauth() {
		super();
	}
	

	public Otpauth(Integer otpid, User userid, String otp) {
		super();
		this.otpid = otpid;
		this.userid = userid;
		this.otp = otp;
	}


	public Integer getOtpid() {
		return otpid;
	}

	public void setOtpid(Integer otpid) {
		this.otpid = otpid;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}


	public LocalDateTime getCreatedat() {
		return createdat;
	}


	public void setCreatedat(LocalDateTime createdat) {
		this.createdat = createdat;
	}
	
	
}
