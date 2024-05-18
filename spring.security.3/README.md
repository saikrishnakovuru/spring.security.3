
SpringSecurity secures our application through authentication and authorization.

`UserDetailsService` --> Authentication.
`SecurityFilterChain` --> Authorization.
`UserDetails` --> To create an User.

```java

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { }
```

We must annotate our class with `@EnableMethodSecurity` to enable the role authorization in method level.


### Authentication

```java
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
```

### Authorization

```java
@Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth ->
                    auth.requestMatchers("/products/welcome").permitAll()
                            .requestMatchers("/products/**")
                            .authenticated()
            )
            .httpBasic(Customizer.withDefaults()).build();
  }
  
  // All the users are able to reach the end point "/products/welcome" but not the "/products/**" because we made it authorized
  // and only authorized users will be able to access it. 
```



In Authentication step we created 2 users and only those users are able to authenticate into the application. We also gave specific roles to every user and let's see them in action.

```java
  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<Product> getAllTheProducts() {
    return service.getProducts();
  }
  
  // With this step only `ADMIN` will be able to call "/all".

  // To use `@PreAuthorized()` we need `@EnableMethodSecurity` in the config class.
```