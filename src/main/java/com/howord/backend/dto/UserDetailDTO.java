package com.howord.backend.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class UserDetailDTO {
	
	@NotBlank(message = "userid 為空值")
	private String userId;
	
	@Email(message = "Email 格式錯誤")
    @NotBlank(message = "收件人 Email 不可為空")
	private String email;
	
	private Date registerDate;
	
	@NotBlank(message = "fullName 不可為空")
	private String fullName;
	
	@NotBlank(message = "nickName 不可為空")
	private String nickName;
	
	@NotBlank(message = "gender 不可為空")
	private String gender;
	
	@NotBlank(message = "phone 不可為空")
	private String phone;
	
	@NotNull(message = "birth 不可為空")
	private Date birth;
	
	private Integer isEnabled;
	
	private String photo;
	
	private String authority;
	
//	private String password;

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

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	
	
}
