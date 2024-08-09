package com.ecommerce.common.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "states")
public class State{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(nullable = false, length = 45)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	
	
	public State(Integer id, String name, Country country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}

	public State() {
	}

	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCoutry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "State [id=" + id + ", name=" + name + ", country=" + country + "]";
	}
	
	
	
}
