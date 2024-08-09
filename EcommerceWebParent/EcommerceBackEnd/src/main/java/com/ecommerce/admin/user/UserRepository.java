package com.ecommerce.admin.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.admin.paging.SearchRepository;
import com.ecommerce.common.entity.User;
@Repository
@EnableJpaRepositories
public interface UserRepository extends SearchRepository<User, Integer>{
	@Query("SELECT u FROM User u where u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	User findUserByEmail(String email);
	 
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	   @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
	    Page<User> findAll(String keyword, Pageable pageable);
	
//	 Page<User> findByFirstNameContaining(String keyword, Pageable pageable);
}
