package com.lal.b_connect.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    UserDetailsUserService userDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(
                            "/",
                            "/index.html",
                            "/*.json",
                            "/*.js",
                            "/*.css",
                            "/assets/**",
                            "/flutter_service_worker.js",
                            "/manifest.json"
                    ).permitAll();
                    registry.requestMatchers("/b-connect/signup").permitAll();
                    registry.requestMatchers("/b-connect/login").permitAll();
                    registry.requestMatchers("/b-connect/saveProfilePhoto").authenticated();
                    // Permit all routes for Flutter web
                    registry.requestMatchers("/**").permitAll();

//                    registry.requestMatchers("autocab/driver/**").hasRole("DRIVER");
//                    registry.requestMatchers("autocab/user/**").hasRole("USER");
                    registry.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())  // Enable HTTP Basic Auth
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)// Disable form login
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return  userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//        public PasswordEncoder passwordEncoder() {
//        return  new BCryptPasswordEncoder();
//    }

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        UserData normalUser = User.builder()
//                .username("user")
//                .password("lal")
//                .roles("USER")
//                .build();
//        UserData adminUser = User.builder()
//                .username("admin")
//                .password("lal")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(adminUser, normalUser);
//    }




}