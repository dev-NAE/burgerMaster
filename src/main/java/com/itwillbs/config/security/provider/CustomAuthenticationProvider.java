package com.itwillbs.config.security.provider;

import com.itwillbs.config.security.exception.LoginException;
import com.itwillbs.config.security.exception.LoginExceptionResult;
import com.itwillbs.entity.Manager;
import com.itwillbs.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SecurityService securityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("CustomAuthenticationProvider authenticate");
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        log.info("username:{}", username);

        // 사용자 정보 조회
        Manager managerInfo = securityService.getManagerByManagerId(username);

        // password 일치 여부 체크
        if(!bCryptPasswordEncoder.matches(password, managerInfo.getPass()))
            throw new LoginException(LoginExceptionResult.NOT_CORRECT);

        // 권한 리스트
        String[] managerRoles = managerInfo.getManagerRole().split(",");
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(String role : managerRoles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        // return UsernamePasswordAuthenticationToken
        // 3번째가 권한을 받는 매개 변수
        // 권한은 Collections을 받을 수 있기 때문에 arrayList나 map 입력 가능
        return new UsernamePasswordAuthenticationToken(
                managerInfo.getManagerId(),
                managerInfo.getPass(),
                grantedAuthorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
