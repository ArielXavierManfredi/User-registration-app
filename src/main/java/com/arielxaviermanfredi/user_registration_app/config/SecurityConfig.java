package com.arielxaviermanfredi.user_registration_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.arielxaviermanfredi.user_registration_app.service.UserServiceImpl;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

        
    @Autowired
    private CustomLoginSuccessHandler customSuccessLoginHandler;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/usuario/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/denuncia", "/denuncia/formulario", "/denuncia/salvarDenuncia").permitAll()
                    .requestMatchers("/denuncia/**").hasRole("ADMIN")
                    .requestMatchers("/contato", "/contato/salvarContato").permitAll()
                    .requestMatchers("/contato/**").hasRole("ADMIN")
                    .requestMatchers("/areaDoCliente/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/js/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/img/**").permitAll()
                    .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("name")
                        .passwordParameter("password")
                        .successHandler(customSuccessLoginHandler)
                        .failureUrl("/login")
                        .isCustomLoginPage())
                .logout(logout -> logout
                        .logoutSuccessUrl("/"))
                .authenticationProvider(authenticationProvider())
                .build();
    }
}