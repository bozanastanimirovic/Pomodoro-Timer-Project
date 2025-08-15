package com.example.backend.security;

import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.TeamRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.jwt.AuthEntryPointJwt;
import com.example.backend.security.jwt.AuthTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;




    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/auth/public/**")
                        .ignoringRequestMatchers("/api/**")
        );

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/notif/**"));

        http.cors(
                cors -> cors.configurationSource(corsConfigurationSource())
        );
        http.exceptionHandling(exception
                -> {exception.authenticationEntryPoint(unauthorizedHandler);
                    exception.accessDeniedHandler(new CustomAccessDeniedHandler());
                });
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:4200"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      TeamRepository teamRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
//            Role userRole = roleRepository.findByRoleName(UserRole.ROLE_USER)
//                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_USER)));
//
//            Role adminRole = roleRepository.findByRoleName(UserRole.ROLE_ADMIN)
//                    .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_ADMIN)));
//
//            if (!userRepository.existsByUsername("user1")) {
//                User user1 = new User("user1", "user1@example.com", "name1", "surname2",
//                        passwordEncoder.encode("password1"));
//                user1.setRole(userRole);
//                userRepository.save(user1);
//            }
//
//            if (!userRepository.existsByUsername("admin")) {
//                User admin = new User("admin", "admin@example.com", "name2", "surname2",
//                        passwordEncoder.encode("adminPass"));
//                admin.setRole(adminRole);
//                userRepository.save(admin);
//            }
        };
    }

}
