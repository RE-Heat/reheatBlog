package com.reheat.reheatlog.annotation;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = ReheatMockSecurityContext.class)
public @interface ReheatlogMockUser {
    String name() default "리히트";

    String email() default "reheat1540@gmail.com";

    String password() default "";
}
