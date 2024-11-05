package com.formation_full_stack_java_angular.rest_api.securityConfig;

import com.formation_full_stack_java_angular.rest_api.services.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtTokenProvider {

    // Clé secrète pour signer les tokens
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 1 jour en millisecondes
		//Date expiryDate = new Date(now.getTime() + 3600000);// 1 heure en millisecondes

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        String username = jwtParser.parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
