package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 授权异常
 */
public class AuthenticationException extends HttpException {

    @Getter
    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    @Getter
    protected int code = Code.UN_AUTHENTICATION.getCode();

    public AuthenticationException() {
        super(Code.UN_AUTHENTICATION.getDescription(), Code.UN_AUTHENTICATION.getCode());
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(int code) {
        super(Code.UN_AUTHENTICATION.getDescription(), code);
        this.code = code;
    }


    public AuthenticationException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
