package com.spring.boot.application.common.auth;

import com.spring.boot.application.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizeValidator {
    UserRole[] value() default UserRole.ADMIN;
}
