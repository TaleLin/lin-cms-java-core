package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 令牌无效异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class TokenInvalidException extends HttpException {

    private static final long serialVersionUID = -7844470320210708005L;

    protected int code = Code.TOKEN_INVALID.getCode();

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public TokenInvalidException() {
        super(Code.TOKEN_INVALID.getDescription(), Code.TOKEN_INVALID.getCode());
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(int code) {
        super(Code.TOKEN_INVALID.getDescription(), code);
        this.code = code;
    }

    public TokenInvalidException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }
}
