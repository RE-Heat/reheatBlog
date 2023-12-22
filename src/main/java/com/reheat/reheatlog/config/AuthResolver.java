package com.reheat.reheatlog.config;

import com.reheat.reheatlog.config.data.UserSession;
import com.reheat.reheatlog.domain.Session;
import com.reheat.reheatlog.exception.UnAuthorized;
import com.reheat.reheatlog.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (servletRequest == null){
            log.error("servletRequest null");
            throw new UnAuthorized();
        }

        Cookie[] cookies = servletRequest.getCookies();

        if (cookies.length == 0) {
            log.error("쿠키가 없음");
            throw new UnAuthorized();
        }

        String accessToken = cookies[0].getValue();

        // 데이터베이스 사용자 확인 작업
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnAuthorized::new);

        return new UserSession(session.getUser().getId());
    }
}
