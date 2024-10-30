package com.itwillbs.config.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * Login Exception
 * <p>
 * Spring Security Login Fail Handler 동작을 위해
 * AuthenticationException 상속
 * </p>
 *
 * @see org.springframework.security.core.AuthenticationException AuthenticationException
 */
@Getter
public class LoginException extends AuthenticationException {

    public LoginException(LoginExceptionResult loginExceptionResult) {
        super(loginExceptionResult.getMessage());
    }
}
