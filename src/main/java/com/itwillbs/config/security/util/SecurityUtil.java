package com.itwillbs.config.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUtil {
    public static String getUserId(){
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }
    // Collection을 상속 받는 클래스로 받으면 됩니다
//    public static Collection<? extends GrantedAuthority> getUserAuthorities(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        authorities.stream().
//        List<GrantedAuthority> roles = new ArrayList<>();
//        roles = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList();
//
//
//        return roles;
//    }
    public static Boolean isAuthenticated(){
        return SecurityContextHolder.getContext()
                .getAuthentication().isAuthenticated();
    }
}
