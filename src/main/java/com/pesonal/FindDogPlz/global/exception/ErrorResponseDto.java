package com.pesonal.FindDogPlz.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ErrorResponseDto {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(final ErrorCode e, final String message) {
        return ResponseEntity
                .status(e.getStatus())
                .body(of(e, message));
    }

    private static ErrorResponseDto of(final ErrorCode e, final String message) {
        return ErrorResponseDto.builder()
                .status(e.getStatus().value())
                .code(e.name())
                .message(message)
                .build();
    }
}
