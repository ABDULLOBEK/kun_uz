package com.example.config;

import com.example.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

   /* @Bean
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
    }*/

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication (login,password)
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    private PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MD5Util.encode(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization (ROLE)
        http.authorizeHttpRequests((c)->
                c.requestMatchers("/api/v1/auth/**").permitAll()
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
                    .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
