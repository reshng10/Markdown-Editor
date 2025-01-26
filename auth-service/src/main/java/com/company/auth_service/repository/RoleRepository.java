package com.company.auth_service.repository;

import com.company.auth_service.document.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role,String> {
     Optional<Role> findByName(String roleName);
}
