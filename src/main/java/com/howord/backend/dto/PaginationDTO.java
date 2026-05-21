package com.howord.backend.dto;

public class PaginationDTO {
	private int total_pages;
    private int current_page;
    private boolean has_pre;
    private boolean has_next;
    private String category;
    
	public int getTotal_pages() {
		return total_pages;
	}
	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}
	public int getCurrent_page() {
		return current_page;
	}
	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}
	public boolean isHas_pre() {
		return has_pre;
	}
	public void setHas_pre(boolean has_pre) {
		this.has_pre = has_pre;
	}
	public boolean isHas_next() {
		return has_next;
	}
	public void setHas_next(boolean has_next) {
		this.has_next = has_next;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
    
    
}
