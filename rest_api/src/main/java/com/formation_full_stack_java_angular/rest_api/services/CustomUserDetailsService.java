package com.formation_full_stack_java_angular.rest_api.services;

import com.formation_full_stack_java_angular.rest_api.entities.Role;
import com.formation_full_stack_java_angular.rest_api.entities.User;
import com.formation_full_stack_java_angular.rest_api.repositories.RoleRepository;
import com.formation_full_stack_java_angular.rest_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Rechercher un utilisateur en fonction de son username
        User user = userRepository.findUserByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("L'utilisateur avec identifiant :" + username + "est absent dans le referentiel");
        }

        Optional<Role> roles = roleRepository.findById(user.getId());
        if(user.getRoles() == null && roles.isPresent()) {
            Role role = roles.get();
            user.setRoles( new ArrayList<>());
            user.getRoles().add(role);
        }
        else {
            throw new UsernameNotFoundException("Aucun rôle n'est associé à ce username :" + username);
        }
        List<UserCustomAuthority> authorityList = user.getRoles().stream().map( role -> new UserCustomAuthority(role.getName())).toList();

        return new org.springframework.security.core.userdetails.User(user.getUsername(),passwordEncoder.encode(user.getPassword()), authorityList);
    }
}
