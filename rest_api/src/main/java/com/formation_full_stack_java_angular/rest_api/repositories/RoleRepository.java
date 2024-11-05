package com.formation_full_stack_java_angular.rest_api.repositories;

import com.formation_full_stack_java_angular.rest_api.entities.Role;
import com.formation_full_stack_java_angular.rest_api.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>, RoleRepositoryCustom {

    @Query("SELECT r from Role r WHERE r.name = : rolename")
    Role findRoleByName(String rolename);
}
