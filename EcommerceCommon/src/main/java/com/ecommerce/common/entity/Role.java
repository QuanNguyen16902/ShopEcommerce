package com.ecommerce.common.entity;

import java.util.Objects;
//import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length = 40, nullable = false, unique = true)
	private String name;

	@Column(length = 150, nullable = false)
	private String description;

	public Role() {
	}

	public Role(Integer id) {
		this.id = id;
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
//	  @ManyToMany(mappedBy = "users")
//	    private Set<User> users = new HashSet<>();
}
