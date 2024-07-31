package com.pesonal.FindDogPlz.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "회원만 가능한 기능입니다.");

    private final HttpStatus status;
    private final String message;
}
