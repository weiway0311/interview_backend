package com.howord.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
	private String id;
    private String title;
    private String category;
    private String content;
    private String description;
    private String imageUrl;
    private List<String> imagesUrl;
    private int isEnabled;
    private BigDecimal originPrice;
    private BigDecimal price;
    private String unit;
    private int num;
    
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public List<String> getImagesUrl() {
		return imagesUrl;
	}
	public void setImagesUrl(List<String> imagesUrl) {
		this.imagesUrl = imagesUrl;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public BigDecimal getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(BigDecimal originPrice) {
		this.originPrice = originPrice;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
    
    
}
