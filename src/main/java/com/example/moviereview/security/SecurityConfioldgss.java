// package com.example.moviereview.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// @EnableMethodSecurity
// public class SecurityConfig {

//     private final JwtAuthFilter jwtAuthFilter;
//     private final List<String> ALLOWED_ORIGINS = Arrays.asList(
//         "http://localhost:3000",
//         "http://localhost:8080"
//     );

//     public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
//         this.jwtAuthFilter = jwtAuthFilter;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     http
//         .csrf(csrf -> csrf.disable())
//         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//         .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/profile.html", "/movie.html", "/styles.css", "/app.js").permitAll()
//             .requestMatchers("/api/auth/**").permitAll()
//             .requestMatchers(HttpMethod.GET, "/api/movies/**", "/api/reviews/**").permitAll()
//             .anyRequest().authenticated()
//         )
//         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//         return http.build();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//     CorsConfiguration config = new CorsConfiguration();
//     config.setAllowCredentials(true);
//     config.addAllowedOriginPattern("*");
//     config.addAllowedHeader("*");
//     config.addAllowedMethod("GET");
//     config.addAllowedMethod("POST");
//     config.addAllowedMethod("PUT");
//     config.addAllowedMethod("DELETE");
//     config.addAllowedMethod("OPTIONS");
//     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//     source.registerCorsConfiguration("/**", config);
//     return source;
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//         return configuration.getAuthenticationManager();
//     }
// }


