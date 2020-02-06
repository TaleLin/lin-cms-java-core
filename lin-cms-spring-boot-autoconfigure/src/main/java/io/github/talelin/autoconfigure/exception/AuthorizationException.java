package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * 认证异常
 */
public class AuthorizationException extends HttpException {

    @Getter
    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    @Getter
    protected int code = Code.UN_AUTHORIZATION.getCode();

    public AuthorizationException() {
        super(Code.UN_AUTHORIZATION.getDescription());
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(int code) {
        super(Code.UN_AUTHORIZATION.getDescription());
        this.code = code;
    }

    public AuthorizationException(String message, int code) {
        super(message);
        this.code = code;
    }
}
