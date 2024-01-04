package com.reheat.reheatlog.annotation;

import com.reheat.reheatlog.config.UserPrincipal;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

@RequiredArgsConstructor
public class ReheatMockSecurityContext implements WithSecurityContextFactory<ReheatlogMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(ReheatlogMockUser annotation) {
        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();
        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal
                , user.getPassword()
                , List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
