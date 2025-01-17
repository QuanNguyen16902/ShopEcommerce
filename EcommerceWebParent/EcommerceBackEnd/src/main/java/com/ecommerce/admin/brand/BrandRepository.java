package com.ecommerce.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.common.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
	public Long countById(Integer id);
	
	public Brand findByName(String name);
 
	@Query("SELECT b FROM Brand b where b.name LIKE %?1%")
	public Page<Brand> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
	
	
}
