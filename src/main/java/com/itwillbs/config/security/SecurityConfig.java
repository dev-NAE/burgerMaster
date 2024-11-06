package com.itwillbs.config.security;

import com.itwillbs.config.security.handler.*;
import com.itwillbs.config.security.provider.CustomAuthenticationProvider;
import com.itwillbs.entity.Manager;
import com.itwillbs.service.ManagerService;
import com.itwillbs.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityService securityService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLoginFailHandler customLoginFailHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] urlsToBePermittedAll = {
                "/",
                "/login/**",
                "/error/**",
                "/css/**",
                "/js/**",
                "/dist/**",
                "/img/**",
                "/plugins/**"
        };
        String[] masterDataUrls = {
                "/masterdata/**"
        };
        String[] TxUrls = {
                "/tx/**"
        };
        String[] mfcUrls = {
                "/mf/**"
        };
        String[] qualityUrls ={
                "/quality/**",
                "/defective/**"
        };

        // url 접근 제한
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(urlsToBePermittedAll).permitAll()
                .requestMatchers("/manager/**").hasRole("ADMIN")
                .requestMatchers(masterDataUrls).hasAnyRole("MASTERDATA", "ADMIN")
                .requestMatchers(TxUrls).hasAnyRole("TRANSACTION", "ADMIN")
                .requestMatchers("/inven/**").hasAnyRole("INVENTORY", "ADMIN")
                .requestMatchers(mfcUrls).hasAnyRole("MANUFACTURE", "ADMIN")
                .requestMatchers(qualityUrls).hasAnyRole("QUALITY", "ADMIN")
                .anyRequest().authenticated()
        );
        // session 정책
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );
        // 자동 로그인
        http.rememberMe(rememberMe -> rememberMe
                .rememberMeServices(rememberMeServices(userDetailsService(securityService)))
        );
        // 로그인 처리
		http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successForwardUrl("/main")
                .successHandler(customLoginSuccessHandler)
                .failureHandler(customLoginFailHandler)
        );

        // 로그아웃 처리
        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .deleteCookies("JSESSIONID","remember-me")
                .invalidateHttpSession(true)
                .permitAll());
        http.exceptionHandling(conf -> conf
                .authenticationEntryPoint(customAuthenticationEntryPointHandler)
                .accessDeniedHandler(customAccessDeniedHandler)
        );
        return http.build();
    }
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder, securityService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        CustomAuthenticationProvider authProvider = customAuthenticationProvider();
        return new ProviderManager(authProvider);
    }
    @Bean
    public UserDetailsService userDetailsService(SecurityService securityService){
        return username -> {
            Manager manager = securityService.getManagerByManagerId(username);
            // 권한 리스트
            String[] managerRoles = manager.getManagerRole().split(",");
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for(String role : managerRoles){
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            return new User(manager.getManagerId(), manager.getPass(), grantedAuthorities);
        };
    }
    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService){
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(
                "testKey",
                userDetailsService,
                TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256);
        rememberMe.setParameter("remember-me");
        rememberMe.setAlwaysRemember(true);
        rememberMe.setTokenValiditySeconds(24*60*60);

        return rememberMe;
    }
}
