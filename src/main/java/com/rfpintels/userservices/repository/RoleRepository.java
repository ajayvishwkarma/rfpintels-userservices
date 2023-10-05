package com.rfpintels.userservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rfpintels.userservices.model.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String>{


	Role findByRoleName(String roleName);

}
