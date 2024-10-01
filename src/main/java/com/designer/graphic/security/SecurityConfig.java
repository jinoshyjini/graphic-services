package com.designer.graphic.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Disable CSRF protection (make sure it's what you want)
                .cors().and()      // Enable CORS if needed (configure as per your requirements)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/images/upload").permitAll()  // Allow image uploads
                        .requestMatchers("/images/all").permitAll()      // Allow access to all images
                        .requestMatchers("/images/image/**").permitAll() // Allow image retrieval
                        .requestMatchers("/images/category/**").permitAll() // Allow access to category images
                        .requestMatchers("/videos/upload").permitAll()   // Allow video uploads
                        .requestMatchers("/videos/all").permitAll()      // Allow access to all videos
                        .requestMatchers("/videos/**").permitAll()
                        .anyRequest().authenticated()  // Other requests require authentication
                )
                .formLogin().disable();  // Disable form login if not needed (remove if using a login form)

        return http.build();
    }
}
