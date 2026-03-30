package com.example.demo.security;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable()) 
        .authorizeHttpRequests(auth -> auth
            // Tady už to není permitAll! Musíte být přihlášená.
        
    .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users/register").permitAll()
    .requestMatchers("/error").permitAll() // Přidejte tuhle řádku
    .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_USER", "USER")
    .anyRequest().authenticated()
           
        )
        // KLÍČOVÁ ZMĚNA: Zapneme Basic Auth (předávání jména a hesla v hlavičce)
        .httpBasic(withDefaults()); 

    return http.build();
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200", 
            "https://weekly-calendar-frontend.vercel.app",
            "https://www.tydenni-kalendar.cz",
            "https://www.kalendar2026.cz"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
public CommandLineRunner secretHashGenerator() {
    return args -> {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String mojelesslo = "heslo123";
        String hash = encoder.encode(mojelesslo);
        System.out.println("=======================================");
        System.out.println("TVŮJ NOVÝ BEZPEČNÝ HASH: " + hash);
        System.out.println("=======================================");
    };
}

    @Bean
public PasswordEncoder passwordEncoder() {

   return new BCryptPasswordEncoder();
   // return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
}
}