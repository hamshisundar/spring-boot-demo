package com.jvlcode.spring_boot_demo.config;

import com.jvlcode.spring_boot_demo.security.JwtFilter;
import com.jvlcode.spring_boot_demo.services.CustomUserDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private JwtFilter jwtfilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.authorizeHttpRequests(authz->
                        authz.requestMatchers(HttpMethod.POST,"/api/user").permitAll()
                                .requestMatchers("/api/user/**").authenticated()
                                .anyRequest().permitAll())
//                .formLogin(form->form.permitAll().defaultSuccessUrl("/dashboard"))    // we have set Form Login for URL Authentication Check Practice but the Game is different Here
                .csrf(csrf->csrf.disable()
                        .sessionManagement(sess ->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                );
        return http.build();
    }
// userDetailsServices ---------> CustomUserDetailServices
    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsServices();
    }
//DaoAuthenticationProvider --------> UserDetailsServices
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authprovider = new DaoAuthenticationProvider(userDetailsService());
        authprovider.setPasswordEncoder(passwordEncoder());
        return authprovider;
    }
//AuthenticationManager-------->DaoAuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(List.of(authenticationProvider()));
    }
//Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}