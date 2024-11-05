package com.formation_full_stack_java_angular.rest_api.repositories;

import com.formation_full_stack_java_angular.rest_api.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long>, UserRepositoryCustom {

    @Query("SELECT u from User u WHERE u.username = :username")
    User findUserByName(String username);
}
