package com.formation_full_stack_java_angular.rest_api.securityConfig;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }
}
