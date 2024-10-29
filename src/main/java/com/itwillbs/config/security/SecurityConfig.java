package com.itwillbs.config.security;

import com.itwillbs.config.security.handler.CustomLoginFailHandler;
import com.itwillbs.config.security.handler.CustomLoginSuccessHandler;
import com.itwillbs.config.security.handler.CustomLogoutSuccessHandler;
import com.itwillbs.config.security.provider.CustomAuthenticationProvider;
import com.itwillbs.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AdminService adminService;

    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailHandler customLoginFailHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] urlsToBePermittedAll = {
                "/",
                "/main/**",
                "/admin/login/**",
                "/login/**",
                "/admin/logout/**",
                "/css/**",
                "/js/**",
                "/dist/**",
                "/img/**",
                "/plugins/**"
        };
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(urlsToBePermittedAll).permitAll()
//                .anyRequest().authenticated()
        );
		http.formLogin(formLogin -> formLogin
                .loginPage("/admin/login")
                .loginProcessingUrl("/active/login")
                .successForwardUrl("/main")
                .successHandler(customLoginSuccessHandler)
                .failureHandler(customLoginFailHandler)
        );

        http.logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        return http.build();
    }
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder(), adminService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        CustomAuthenticationProvider authProvider = customAuthenticationProvider();
        return new ProviderManager(authProvider);
    }


}
