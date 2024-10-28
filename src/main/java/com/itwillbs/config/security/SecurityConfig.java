package com.itwillbs.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 스프링 시큐리티 기능 비활성화 (H2 DB 접근을 위해)
//	@Bean
//	public WebSecurityCustomizer configure() {
//		return (web -> web.ignoring()
//				.requestMatchers(toH2Console())
//				.requestMatchers("/h2-console/**")
//		);
//	}

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String[] urlsToBePermittedAll = {
                "/",
                "/main/**",
                "/admin/login/**",
                "/admin/logout/**",
                "/css/**",
                "/js/**",
                "/dist/**",
                "/img/**",
                "/plugins/**"
        };
        http
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(urlsToBePermittedAll).permitAll()
                        .anyRequest().authenticated())
                // 폼 로그인은 현재 사용하지 않음
				.formLogin(formLogin -> formLogin
						.loginPage("/admin/login")
						.defaultSuccessUrl("/main"))
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true));
        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
