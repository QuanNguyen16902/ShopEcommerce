package com.ecommerce.common.entity;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Country{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(length = 45, nullable = false)
	private String name;
	
	@Column(length = 5, nullable = false)
	private String code;

	@OneToMany(mappedBy = "country")
	private Set<State> states;
	
	
	public Country() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Country( String name, String code) {
		this.name = name;
		this.code = code;
	}
	public Country( String name) {
		this.name = name;
	}
	
	
	public Country(Integer id) {
		this.id = id;
	}

	public Country(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	
	
	public Country(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code + "]";
	}
	

	
}
