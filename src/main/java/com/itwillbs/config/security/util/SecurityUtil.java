package com.itwillbs.config.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SecurityUtil {
    // 현제 접속 중인 유저의 아이디를 반환(로그인이 안되어 있다면 anonymous를 반환)
    public static String getUserId(){
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }
    // 현제 접속 중인 유저의 권한 정보를 ArrayList 형태로 반환
    public static ArrayList<GrantedAuthority> getUserAuthorities(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iteratorAuth = authorities.stream().iterator();
        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        while (iteratorAuth.hasNext()){
            roles.add(iteratorAuth.next());
        }


        return roles;
    }
    // 현제 접속 중인 유저가 인증 된 유저인지 확인
    public static Boolean isAuthenticated(){
        return SecurityContextHolder.getContext()
                .getAuthentication().isAuthenticated();
    }
}
