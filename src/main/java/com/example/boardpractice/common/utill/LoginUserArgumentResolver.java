package com.example.boardpractice.common.utill;

import com.example.boardpractice.web.dto.user.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;
    public LoginUserArgumentResolver(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
    // 파라미터에 @LoginUser 있고, sessionUser이면 지원
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginUser.class) != null
                && parameter.getParameterType().equals(SessionUser.class);
    }

    //세션에서 유저 정보를 넣어줌
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return httpSession.getAttribute("loginUser");
    }
}
