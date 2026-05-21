package com.howord.backend.user;

import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_detail")
@Component
public class UserDetail {
	
	@Id @Column(name = "user_id")
	private String userId;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "register_date")
	private Date registerDate;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "nick_name")
	private String nickName;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "birth")
	private Date birth;
	
	@Column(name = "is_enabled")
	private int isEnabled;
	
	@Column(name = "authority")
	private String authority;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "photo")
	private String photo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
}
