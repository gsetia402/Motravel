package org.moto.motravel.security;

import org.moto.motravel.security.jwt.AuthTokenFilter;
import org.moto.motravel.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> 
                auth.requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api-docs/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                    // Allow public access to vehicle browsing endpoints
                    .requestMatchers(new AntPathRequestMatcher("/api/vehicles", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/vehicles/*", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/vehicles/available", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/vehicles/nearby", "GET")).permitAll()
                    // Allow public access to hidden gems browsing endpoints
                    .requestMatchers(new AntPathRequestMatcher("/api/hidden-gems", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/hidden-gems/*", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/hidden-gems/nearby", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/hidden-gems/stats", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/states", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/states/*", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/states/search", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/states/*/hidden-gems", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/adventure-types", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/adventure-types/*", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/adventure-types/search", "GET")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/adventure-types/*/hidden-gems", "GET")).permitAll()
                    .anyRequest().authenticated()
            );
        
        // Enable H2 console frame options
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
