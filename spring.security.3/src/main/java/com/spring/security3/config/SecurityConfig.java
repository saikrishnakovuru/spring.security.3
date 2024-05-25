package com.spring.security3.config;

import com.spring.security3.service.UserDetailsFromRepositoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
//    UserDetails admin = User.withUsername("sai")
//            .password(passwordEncoder.encode("sai"))
//            .roles("ADMIN")
//            .build();
//
//    UserDetails user = User.withUsername("uj")
//            .password(passwordEncoder.encode("uj"))
//            .roles("USER")
//            .build();
//
//    return new InMemoryUserDetailsManager(admin, user);
    return new UserDetailsFromRepositoryService();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
            // without the below line couldn't load the UI in `h2-console`.
            .headers(headers -> headers.frameOptions(options -> options.disable()))
            .authorizeHttpRequests(auth ->
                    auth.requestMatchers("/products/welcome", "/products/new", "/h2-console/**").permitAll()
                            .requestMatchers("/products/**").authenticated()
            )
            .httpBasic(Customizer.withDefaults()).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }
}
