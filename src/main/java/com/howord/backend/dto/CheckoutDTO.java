package com.howord.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CheckoutDTO {
	
	@NotBlank(message = "收件人姓名不可為空")
    private String recipientName;

    @NotBlank(message = "收件人電話不可為空")
    private String recipientPhone;

    @NotBlank(message = "收件人地址不可為空")
    private String recipientAddress;

    @Email(message = "Email 格式錯誤")
    @NotBlank(message = "收件人 Email 不可為空")
    private String recipientEmail;

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
    
    
	
}
