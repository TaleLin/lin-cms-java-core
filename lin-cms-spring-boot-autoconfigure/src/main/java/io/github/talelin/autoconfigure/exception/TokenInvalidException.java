package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 令牌无效
 */
public class TokenInvalidException extends HttpException {

    @Getter
    protected int code = Code.TOKEN_INVALID.getCode();

    @Getter
    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException() {
        super(Code.TOKEN_INVALID.getDescription());
    }

    public TokenInvalidException(int code) {
        super(Code.TOKEN_INVALID.getDescription());
        this.code = code;
    }

    public TokenInvalidException(String message, int code) {
        super(message);
        this.code = code;
    }
}
