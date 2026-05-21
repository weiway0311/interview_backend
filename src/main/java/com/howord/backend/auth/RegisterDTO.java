package com.howord.backend.auth;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegisterDTO {
	
	@Email(message = "Email 格式錯誤")
	@NotBlank(message = "email 不可為空")
	private String email;
	
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
	
	@NotNull(message = "password 不可為空")
	private String password;
	
	@NotNull(message = "confirmPassword 不可為空")
	private String confirmPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
//	private int userLevel;
//	
//	private String photo;
	
	
	
}
