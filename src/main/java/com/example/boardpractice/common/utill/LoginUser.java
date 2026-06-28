package com.example.boardpractice.common.utill;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 파라미터에만 사용
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
