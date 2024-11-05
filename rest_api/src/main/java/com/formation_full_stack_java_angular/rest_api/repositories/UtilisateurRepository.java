package com.formation_full_stack_java_angular.rest_api.repositories;


import com.formation_full_stack_java_angular.rest_api.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
