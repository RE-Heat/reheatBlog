package com.reheat.reheatlog.config;

import com.reheat.reheatlog.config.data.UserSession;
import com.reheat.reheatlog.exception.UnAuthorized;
import com.reheat.reheatlog.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;

    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("appConfig = {}", appConfig.toString());

        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")) {
            throw new UnAuthorized();
        }

        SecretKey key = appConfig.getJwtKey();

        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws);
            log.info(">>>> {}", claimsJws);

            String userId = claimsJws.getPayload().getSubject();
            return new UserSession(Long.parseLong(userId));

        } catch (JwtException e) {
            throw new UnAuthorized();
        }
    }
}
