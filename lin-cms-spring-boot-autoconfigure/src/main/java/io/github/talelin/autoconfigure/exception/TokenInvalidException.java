package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 令牌无效异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class TokenInvalidException extends HttpException {

    private static final long serialVersionUID = -7844470320210708005L;

    protected int code = Code.TOKEN_INVALID.getCode();

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public TokenInvalidException() {
        super(Code.TOKEN_INVALID.getCode(), Code.TOKEN_INVALID.getDescription());
        super.ifDefaultMessage=true;
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(int code) {
        super(code, Code.TOKEN_INVALID.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public TokenInvalidException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public TokenInvalidException(int code, String message) {
        super(code, message);
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
