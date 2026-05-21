package com.howord.backend.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupon")
public class Coupon {
	
	@Id @Column(name = "id")
	private String id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "is_enabled")
	private int isEnabled;
	
	@Column(name = "percent")
	private int percent;
	
	@Column(name = "due_date")
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
