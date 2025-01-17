package com.ecommerce.common.entity.product;

import com.ecommerce.common.entity.IdBasedEntity;

import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_details")
public class ProductDetail extends IdBasedEntity{
	
	@Column(length = 255, nullable = false)
	private String name;
	@Column(length = 255, nullable = false)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	public ProductDetail() {
		
	}

	public ProductDetail(Integer id, String name, String value, Product product) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public ProductDetail(String name, String value, Product product) {
		this.name = name;
		this.value = value;
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
