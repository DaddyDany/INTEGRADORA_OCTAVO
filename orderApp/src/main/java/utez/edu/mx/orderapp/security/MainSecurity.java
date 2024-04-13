package utez.edu.mx.orderapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final String[] whiteList = {
            "/api/auth/**",
            "/api/accounts/**",
            "/api/services/**",
            "/api/packages/**",
            "/api/combos/**",
            "/api/orders/**",
            "/api/reviews/**",
            "/api/payments/**"
    };
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
                        req.requestMatchers(whiteList).permitAll()
//                                .requestMatchers("/api/accounts/create-worker").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/create-admin").permitAll()
//                                .requestMatchers("/api/accounts/update-admin/info").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/update-admin/profile-pic").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/administrators").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/update-worker").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/create-common").permitAll()
//                                .requestMatchers("/api/accounts/update/info/**").hasAuthority("COMMON_USER")
//                                .requestMatchers("/api/accounts/update/profile-pic/**").hasAuthority("COMMON_USER")
//                                .requestMatchers("/api/accounts/confirm-account").permitAll()
//                                .requestMatchers("/api/accounts/confirm-worker-account").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/confirm-worker-account").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/confirm-admin-account").hasAuthority("ADMIN")
//                                .requestMatchers("/api/accounts/profile").hasAnyAuthority("ADMIN", "COMMON_USER", "WORKER")
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
