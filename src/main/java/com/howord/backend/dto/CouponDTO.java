package com.howord.backend.dto;


public class CouponDTO {
	
	private String id;
	
	private String title;
	
	private String code;
	
	private int isEnabled;
	
	private int percent;
	
	private Long dueDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public Long getDueDate() {
		return dueDate;
	}

	public void setDueDate(Long dueDate) {
		this.dueDate = dueDate;
	}
	
	
	
}
