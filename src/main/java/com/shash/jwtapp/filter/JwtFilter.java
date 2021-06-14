package com.shash.jwtapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shash.jwtapp.service.JwtService;
import com.shash.jwtapp.service.MyUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                final String authHeader = request.getHeader("Authorization");
                String username=null,jwt=null;
                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    jwt = authHeader.substring(7);
                    username = jwtService.extractUsername(jwt);
                }
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    UserDetails details = userDetailService.loadUserByUsername(username);
                    boolean isValidToken = false;
                    try {
                        isValidToken = jwtService.validateToken(jwt, details);
                    } catch (SignatureException e) {
                        isValidToken = false;
                    }
                    if(isValidToken){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(details, null , details.getAuthorities());
                        usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
    }
    
}
