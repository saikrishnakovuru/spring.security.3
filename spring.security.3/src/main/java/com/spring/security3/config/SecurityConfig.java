package com.spring.security3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin = User.withUsername("sai")
            .password(passwordEncoder.encode("sai"))
            .roles("ADMIN")
            .build();

    UserDetails user = User.withUsername("uj")
            .password(passwordEncoder.encode("uj"))
            .roles("USER")
            .build();

    return new InMemoryUserDetailsManager(admin, user);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
            // without the below line couldn't load the UI in `h2-console`.
            .headers(headers -> headers.frameOptions(options -> options.disable()))
            .authorizeHttpRequests(auth ->
                    auth.requestMatchers("/products/welcome").permitAll()
                            .requestMatchers("/products/**").authenticated()
                            .requestMatchers("/h2-console/**").permitAll()
            )
            .httpBasic(Customizer.withDefaults()).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
