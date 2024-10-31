package com.itwillbs.config.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityUtil {
    public static String getUserId(){
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }
    // Collection을 상속 받는 클래스로 받으면 됩니다
    public static Collection<? extends GrantedAuthority> getUserAuthorities(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
    public static Boolean isAuthenticated(){
        return SecurityContextHolder.getContext()
                .getAuthentication().isAuthenticated();
    }
}
