package com.pesonal.FindDogPlz.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없는 데이터입니다."),
    DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "회원만 가능한 기능입니다."),
    POINT_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "좌표 변환 중 에러가 발생했습니다. 좌표를 다시 설정해주세요");

    private final HttpStatus status;
    private final String message;
}
