package com.jvlcode.spring_boot_demo.security;

import com.jvlcode.spring_boot_demo.services.CustomUserDetailsServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //Get the Header and Save as AuthHeader
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try{
            String username = jwtUtil.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
               UserDetails userDetails = customUserDetailsServices.loadUserByUsername(username);

               if(jwtUtil.validToken(token, userDetails)) {
                   UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                           userDetails.getUsername(),
                           userDetails.getPassword(),
                           userDetails.getAuthorities()
                   );
                   //to get Authenticated user Details
                   authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                   //To let Springboot know about the Authenticated User
                   SecurityContextHolder.getContext().setAuthentication(authenticationToken);
               }
            }
        } catch (Exception e) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("error", "Invalid Token");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(responseMap);

            response.getWriter().write(jsonString);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;

        }
        // if Everything went well pass to Next Filter which is Security config
        filterChain.doFilter(request, response);
    }

    }

