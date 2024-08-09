package com.ecommerce.admin.user;



import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.common.entity.Role;

@Repository
@EnableJpaRepositories
public interface RoleRepository extends CrudRepository<Role, Integer> {

}
