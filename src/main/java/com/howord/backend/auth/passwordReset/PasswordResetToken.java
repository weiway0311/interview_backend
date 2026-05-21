package com.howord.backend.auth.passwordReset;

import java.time.LocalDateTime;

import com.howord.backend.user.UserDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@Column(name = "token", nullable = false, length = 64)
    private String token;
	
	@Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
	
	@Column(name = "used", nullable = false)
    private boolean used;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserDetail userDetail;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}


	public boolean isUsed() {
		return used;
	}


	public void setUsed(boolean used) {
		this.used = used;
	}


	public UserDetail getUserDetail() {
		return userDetail;
	}


	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
    
    
    
	
}
