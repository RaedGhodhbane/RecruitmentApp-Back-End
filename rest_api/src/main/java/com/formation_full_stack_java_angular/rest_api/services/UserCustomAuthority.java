package com.formation_full_stack_java_angular.rest_api.services;

import org.springframework.security.core.GrantedAuthority;

public class UserCustomAuthority implements GrantedAuthority {

    private String authority;

    public UserCustomAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
