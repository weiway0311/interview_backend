package com.howord.backend.product;

import java.math.BigDecimal;
import java.util.List;

import com.howord.backend.convert.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "product", indexes = {
	    @Index(name = "idx_product_category", columnList = "category")
	})
public class Product {
	
	@Id @Column(name = "id")
	private String id;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "image_url")
	private String imageUrl;

	@Convert(converter = StringListConverter.class)
	@Column(name = "images_url", columnDefinition = "JSON")
	private List<String> imagesUrl;
    
    @Column(name = "is_enabled")
    private int isEnabled;
    
    @Column(name = "origin_price")
    private BigDecimal originPrice;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "unit")
    private String unit;

    @Column(name = "num")
    private int num;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
