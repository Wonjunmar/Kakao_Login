package com.example.demo.config;

import com.example.demo.config.filter.JwtFilter;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberService memberService;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {

            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/jwt/*").permitAll()
                    .antMatchers("/member/*").permitAll()
                    .antMatchers("/test/*").hasRole("USER")
                    .anyRequest().authenticated();
            http.addFilterBefore(new JwtFilter(memberService, secretKey), UsernamePasswordAuthenticationFilter.class);
            http.formLogin().disable();
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
