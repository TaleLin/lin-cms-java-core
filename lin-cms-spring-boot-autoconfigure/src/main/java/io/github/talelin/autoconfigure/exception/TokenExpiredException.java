package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 令牌过期异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class TokenExpiredException extends HttpException {

    private static final long serialVersionUID = -562886931687729013L;

    protected int code = Code.TOKEN_EXPIRED.getCode();

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public TokenExpiredException() {
        super(Code.TOKEN_EXPIRED.getDescription(), Code.TOKEN_EXPIRED.getCode());
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(int code) {
        super(Code.TOKEN_EXPIRED.getDescription(), code);
        this.code = code;
    }

    public TokenExpiredException(String message, int code) {
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
