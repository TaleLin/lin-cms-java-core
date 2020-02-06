package com.lin.cms.autoconfigure.exception;

import com.lin.cms.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 令牌过期
 */
public class TokenExpiredException extends HttpException {

    @Getter
    protected int code = Code.TOKEN_EXPIRED.getCode();

    @Getter
    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException() {
        super(Code.TOKEN_EXPIRED.getDescription());
    }

    public TokenExpiredException(int code) {
        super(Code.TOKEN_EXPIRED.getDescription());
        this.code = code;
    }

    public TokenExpiredException(String message, int code) {
        super(message);
        this.code = code;
    }
}
