package entities;

import java.time.LocalDateTime;

import enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	Integer userid;

	@Column(name = "user_name")
	String username;

	String email;

	@Column(name = "password_hash")
	String passwordhash;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false, insertable = false, updatable = false)
	LocalDateTime created_at ;

	@Column(nullable = false, insertable = false, updatable = true)
	LocalDateTime updated_at;
	
	
	public User() {
		super();
	}

	public User(String name, String email, Role role, String passwordhash, LocalDateTime created_at, LocalDateTime updated_at) {
		
		super();
		this.username = name;
		this.email = email;
		this.role = role;
		this.passwordhash = passwordhash;
		this.created_at = created_at;
		this.updated_at = updated_at;
		
	}

	public Integer getUser_id() {
		return userid;
	}

	public void setUser_id(Integer user_id) {
		this.userid = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordhash() {
		return passwordhash;
	}

	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}

}
