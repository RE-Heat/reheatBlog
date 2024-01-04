package com.reheat.reheatlog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserPrincipal extends User {
    private final Long userId;

    // role : 역할 -> 관리자, 사용자, 매니저
    // authority : 권한 -> 글쓰기, 글 읽기, 사용자 정지시키기

    public UserPrincipal(com.reheat.reheatlog.domain.User user) {
        super(user.getEmail(), user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                ));

        this.userId = user.getId();
    }

    public Long getUserId() {
        return userId;
    }
}
