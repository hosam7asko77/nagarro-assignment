package com.nagarro.assignment.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        }catch (ExpiredJwtException e){
            tokenNotFoundResponse(response,
                    request,
                    SecurityConstants.EXPIRATION_TIME_ERROR,
                    SecurityConstants.EXPIRED_MESSAGE);
        }catch (JwtException j){
            tokenNotFoundResponse(response,
                    request,
                    SecurityConstants.TOKEN_ERROR,
                    SecurityConstants.TOKEN_MESSAGE);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = getToken(request);
        if (token != null) {
            String userInfo = getUserInfo(token);
            if (userInfo != null) {
                return new UsernamePasswordAuthenticationToken(userInfo, null, grantedAuthority(token));
            }
            return null;
        }
        return null;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getUserInfo(String token) {
        if (token != null) {
            return Jwts.parser()
                    .setSigningKey(SecurityConstants.KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> grantedAuthority(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.KEY).parseClaimsJws(token).getBody();
        String roles = claims.get("roles").toString();
        roles = roles.replace("[", "");
        roles = roles.replace("]", "");
        return Stream.of(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void tokenNotFoundResponse(HttpServletResponse response,
                                       HttpServletRequest request,
                                       String error,
                                       String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.addIntHeader("status", HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", new Date().toString());
        map.put("status", String.valueOf(HttpServletResponse.SC_FORBIDDEN));
        map.put("error", error);
        map.put("message", message);
        map.put("path", request.getServletPath());
        String json = new ObjectMapper().writeValueAsString(map);
        writer.print(json);
        writer.flush();
    }
}

