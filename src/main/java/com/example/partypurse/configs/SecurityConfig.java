package com.example.partypurse.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Добавил роли, но ничего не изменилось
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/room/**").hasAuthority("USER") // или с .authenticated()
                        .requestMatchers("/api/v1/user/hi1").hasAuthority("USER") // или с .authenticated()
                        .requestMatchers("/api/v1/user/hi2").hasAuthority("ADMIN") // или с .authenticated()
                        .requestMatchers("/api/v1/user/hi3").hasRole("USER") // или с .authenticated()
                        .requestMatchers("/api/v1/user/hi4").hasRole("ADMIN") // или с .authenticated()
                        .requestMatchers("/api/v1/user/hi5").authenticated() // или с .authenticated()
                        .requestMatchers("/api/v1/user/info").permitAll() // или с .authenticated()
//                        .requestMatchers("/api/v1/user/**").authenticated()
                        //.requestMatchers("/api/v1/user/delete/").hasAuthority("ADMIN")
                        //.anyRequest().permitAll())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
