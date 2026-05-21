package com.howord.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetDTO {
	
	@NotBlank
	private String token;
	
	@NotBlank
    private String email;
    
	@NotBlank
    private String newPassword;
    
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    
}
