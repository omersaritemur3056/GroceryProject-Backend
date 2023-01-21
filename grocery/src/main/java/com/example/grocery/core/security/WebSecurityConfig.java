package com.example.grocery.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.grocery.core.security.enums.Authority;
import com.example.grocery.core.security.jwt.AuthEntryPointJwt;
import com.example.grocery.core.security.jwt.AuthTokenFilter;
import com.example.grocery.core.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig {

    final AuthEntryPointJwt unauthorizatedHandler;
    final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(AuthEntryPointJwt unauthorizatedHandler, UserDetailsServiceImpl userDetailsService) {
        this.unauthorizatedHandler = unauthorizatedHandler;
        this.userDetailsService = userDetailsService;
    }

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("HEAD");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("PATCH");
        configurationSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(configurationSource);

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizatedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/*",
                        "/api/**",
                        "/swagger-ui/index.html",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/v2/api-docs/**",
                        "/swagger-resources/**")
                .permitAll()
                // .requestMatchers("/api/order/**") //test succeed
                // .hasAnyAuthority(Authority.ADMIN.name(), Authority.USER.name())
                .anyRequest()
                .authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
