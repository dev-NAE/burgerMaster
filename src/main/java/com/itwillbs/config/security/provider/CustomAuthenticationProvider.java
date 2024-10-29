package com.itwillbs.config.security.provider;

import com.itwillbs.config.security.exception.LoginException;
import com.itwillbs.config.security.exception.LoginExceptionResult;
import com.itwillbs.entity.Admin;
import com.itwillbs.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AdminService adminService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider authenticate");
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        log.info("username:{}", username);
        log.info("password:{}", password);
        // 사용자 정보 조회
        Admin adminInfo = adminService.getAdminByAdminId(username);

        // password 일치 여부 체크
        if(!bCryptPasswordEncoder.matches(password, adminInfo.getPass()))
            throw new LoginException(LoginExceptionResult.NOT_CORRECT);

        // return UsernamePasswordAuthenticationToken
        return new UsernamePasswordAuthenticationToken(
                adminInfo.getAdminID(),
                adminInfo.getPass()

        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
