package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication (login,password)
        String password = UUID.randomUUID().toString();
        System.out.println("User Password mazgi: " + password);

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}" + password) // here we have a lot of types, like bycrif
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}" + "12345") // here we have a lot of types, like bycrif
                .roles("ADMIN")
                .build();

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(user, admin));
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization (ROLE)
        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/news/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/region/lang").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/articleType/lang").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/category/lang").permitAll()
                .requestMatchers("/api/v1/attach/**").permitAll()
                .requestMatchers("/api/v1/attach/admin/**").hasAnyRole("ADMIN","MODERATOR")
                .requestMatchers("/api/v1/profile/**").permitAll()
                .requestMatchers("/api/v1/profile/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/profile/**").permitAll()
                .requestMatchers("/api/v1/region/admin","/api/v1/region/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and().httpBasic();
                 http.csrf().disable();
        return http.build();
    }
}
