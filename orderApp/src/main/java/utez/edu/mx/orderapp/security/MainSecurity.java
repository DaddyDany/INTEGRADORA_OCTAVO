package utez.edu.mx.orderapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import utez.edu.mx.orderapp.security.jwt.JwtAuthenticationFilter;
import utez.edu.mx.orderapp.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MainSecurity {
    private final UserDetailsServiceImpl service;

    public MainSecurity(UserDetailsServiceImpl service) {
        this.service = service;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(service);
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter filter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                //ACCOUNTS-CONTROLLER
                                .requestMatchers("/api/accounts/create-admin").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/create-worker").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/workers").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/administrators").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/delete-admin").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/delete-worker").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/update-admin").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/update-user").hasAuthority("COMMON_USER")
                                .requestMatchers("/api/accounts/update-worker").hasAnyAuthority("ADMIN", "WORKER")
                                .requestMatchers("/api/accounts/update-admin/profile-pic").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/update-user/profile-pic").hasAuthority("COMMON_USER")
                                .requestMatchers("/api/accounts/update-worker/profile-pic").hasAuthority("WORKER")
                                .requestMatchers("/api/accounts/confirm-worker-account").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/confirm-admin-account").hasAuthority("ADMIN")
                                .requestMatchers("/api/accounts/profile").hasAnyAuthority("ADMIN", "WORKER", "COMMON_USER")
                                .requestMatchers("/api/accounts/create-common").permitAll()
                                .requestMatchers("/api/accounts/confirm-account").permitAll()
                                .requestMatchers("/api/auth/signin").permitAll()
                                //CATEGORIES-CONTROLLER
                                .requestMatchers(HttpMethod.GET, "/api/services").hasAnyAuthority("COMMON_USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/services").hasAuthority("ADMIN")
                                .requestMatchers("/api/services/packages/from-service").hasAnyAuthority("COMMON_USER")
                                .requestMatchers("/api/services/update-service").hasAnyAuthority("ADMIN")
                                .requestMatchers("/api/services/delete-service").hasAnyAuthority("ADMIN")
                                //COMBOS-CONTROLLER
                                .requestMatchers(HttpMethod.GET, "/api/combos").hasAnyAuthority("COMMON_USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/combos").hasAuthority("ADMIN")
                                .requestMatchers("/api/combos/combo-info").hasAuthority("COMMON_USER")
                                .requestMatchers("/api/combos/update-combo").hasAuthority("ADMIN")
                                .requestMatchers("/api/combos/delete-combo").hasAuthority("ADMIN")
                                //ORDERS-CONTROLLER
                                .requestMatchers(HttpMethod.GET, "/api/orders").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/orders").hasAuthority("COMMON_USER")
                                .requestMatchers( "/api/orders/decline").hasAuthority("ADMIN")
                                .requestMatchers( "/api/orders/accept-and-assign").hasAuthority("ADMIN")
                                .requestMatchers( "/api/orders/complete").hasAuthority("ADMIN")
                                .requestMatchers( "/api/orders/my-orders").hasAuthority("COMMON_USER")
                                .requestMatchers( "/api/orders/worker-assigned-orders").hasAuthority("WORKER")
                                //PAYMENTS-CONTROLLER
                                .requestMatchers( "/api/payments/create-checkout-session").hasAuthority("COMMON_USER")
                                //PACKAGE-CONTROLLER
                                .requestMatchers(HttpMethod.GET, "/api/packages").hasAnyAuthority("ADMIN", "COMMON_USER")
                                .requestMatchers( "/api/packages/package-info-users").hasAuthority("COMMON_USER")
                                .requestMatchers( "/api/packages/update-package").hasAuthority("ADMIN")
                                .requestMatchers( "/api/packages/delete-package").hasAuthority("ADMIN")
                                //REVIEWS
                                .requestMatchers(HttpMethod.GET, "/api/reviews").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/reviews").hasAuthority("COMMON_USER")
                                .requestMatchers( "/api/reviews/my-reviews").hasAuthority("COMMON_USER")
                                .requestMatchers( "/api/reviews/delete").hasAuthority("COMMON_USER")
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)
                .logout(out -> out.logoutUrl("/api/auth/logout").clearAuthentication(true));
        return http.build();
    }
}
