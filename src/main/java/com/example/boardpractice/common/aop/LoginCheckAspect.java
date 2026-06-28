package com.example.boardpractice.common.aop;

import com.example.boardpractice.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LoginCheckAspect {


    @Before("@annotation(com.example.boardpractice.common.utill.LoginRequired)")
    public void checkLogin() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(false);

        // 로그 추가
        if (session != null) {
            System.out.println("세션 ID: " + session.getId());
            System.out.println("세션에 저장된 값: " + session.getAttribute("loginUser"));
        } else {
            System.out.println("세션이 아예 없습니다!");
        }
        if (session.getAttribute("loginUser")==null){
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
    }
}
