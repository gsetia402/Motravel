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
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/auth/vendor-signup").permitAll()
                    .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    // Admin endpoints must have ADMIN authority
                    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                    // Vendor endpoints: accessible to VENDORs and ADMINs
                    .requestMatchers("/api/vendor/**").hasAnyAuthority("ROLE_VENDOR", "ROLE_ADMIN")
                    // Vehicle bookings admin list
                    .requestMatchers("/api/bookings").hasAuthority("ROLE_ADMIN")
                    // Public vehicle endpoints
                    .requestMatchers("/api/vehicles", "/api/vehicles/available", "/api/vehicles/nearby").permitAll()
                    .requestMatchers("/api/vehicles/*").permitAll()
                    // Public hidden gems endpoints
                    .requestMatchers("/api/hidden-gems", "/api/hidden-gems/nearby", "/api/hidden-gems/stats").permitAll()
                    .requestMatchers("/api/hidden-gems/*").permitAll()
                    .requestMatchers("/api/states", "/api/states/search", "/api/states/*", "/api/states/*/hidden-gems").permitAll()
                    .requestMatchers("/api/adventure-types", "/api/adventure-types/search", "/api/adventure-types/*", "/api/adventure-types/*/hidden-gems").permitAll()
                    // Public tours endpoints
                    .requestMatchers("/api/tours", "/api/tours/*", "/api/tours/*/availability").permitAll()
                    .requestMatchers("/api/tours/*/book").permitAll()
                    .anyRequest().authenticated()
            );
        
        // Enable H2 console frame options
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
        configuration.setAllowedMethods(java.util.List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("Authorization","Cache-Control","Content-Type","X-Requested-With","Accept","Origin"));
        configuration.setExposedHeaders(java.util.List.of("Authorization"));
        configuration.setAllowCredentials(true);
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
