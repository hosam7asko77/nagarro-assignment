package com.nagarro.assignment.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.annotations.VisibleForTesting;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.assignment.model.LoginDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authManager) {
        super.setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
        	LoginDetails creds = new ObjectMapper().readValue(request.getInputStream(), LoginDetails.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getUserName(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        String userName = ((User) authResult.getPrincipal()).getUsername();
        Set<String> roles = authResult.getAuthorities().stream()
        	     .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String role = roles.iterator().next();
        String token = crateJwtToken(userName, role);
        initResponse(response,token,role);
    }
    @VisibleForTesting
    private String crateJwtToken(String userName, String role){
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(SecurityConstants.ROLES, Collections.singletonList(role));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.KEY)
                .compact();
    }
    private void initResponse(HttpServletResponse response, String token,String role) throws IOException {
        response.addHeader("token", token);
        response.addHeader("expiresTime", String.valueOf(SecurityConstants.EXPIRATION_TIME));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.addIntHeader("status", HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        PrintWriter  writer = response.getWriter();
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("expiresTime", String.valueOf(SecurityConstants.EXPIRATION_TIME));
        map.put("role", role);
        String json = new ObjectMapper().writeValueAsString(map);
        writer.print(json);
        writer.flush();
    }

}
