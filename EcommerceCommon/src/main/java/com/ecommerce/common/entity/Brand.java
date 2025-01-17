package com.ecommerce.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brand{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(nullable = false, length = 45, unique = true)
	private String name;
	@Column(nullable = false, length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(name = "brands_categories",
				joinColumns = @JoinColumn(name = "brand_id"),
				inverseJoinColumns = @JoinColumn(name = "category_id"))
	Set<Category> categories = new HashSet<>();


	public Brand() {
		
	}
	
	public Brand(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	

	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}

	public Brand(String name) {
		this.name = name;
		this.logo = "brand-logo.png";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	
	
	@Transient
	public String getLogoPath() {
		if(this.id == null || logo == null ) return "/images/image-thumbnail.png";
		return "/brand-logos/" + this.id + "/" + this.logo;
	}
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", categories=" + categories + "]";
	}
	
}
