package com.example.partypurse.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //http.authorizeHttpRequests(req -> req.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll());


        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/user/protected").hasRole("ADMIN")
                                .requestMatchers("/info").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/authority").hasAuthority("READ_PRIVILEGE")
                                .anyRequest().authenticated()
//                        .requestMatchers("/api/v1/room/**").authenticated()
//                        .requestMatchers("/api/v1/user/delete/**").hasAuthority("ADMIN")
//                        .requestMatchers("/api/v1/user/**").authenticated()
//                        .anyRequest().permitAll()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
