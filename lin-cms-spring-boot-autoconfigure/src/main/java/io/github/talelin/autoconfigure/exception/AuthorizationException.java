package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;


/**
 * 认证异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class AuthorizationException extends HttpException {

    private static final long serialVersionUID = -432605618235404747L;

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    protected int code = Code.UN_AUTHORIZATION.getCode();

    public AuthorizationException() {
        super(Code.UN_AUTHORIZATION.getDescription(), Code.UN_AUTHORIZATION.getCode());
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(int code) {
        super(Code.UN_AUTHORIZATION.getDescription(), code);
        this.code = code;
    }

    public AuthorizationException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public int getCode() {
        return code;
    }
}
