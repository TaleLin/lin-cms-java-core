package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 授权异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class AuthenticationException extends HttpException {

    private static final long serialVersionUID = -222891683232481602L;

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    protected int code = Code.UN_AUTHENTICATION.getCode();

    public AuthenticationException() {
        super(Code.UN_AUTHENTICATION.getCode(), Code.UN_AUTHENTICATION.getDescription());
        super.ifDefaultMessage = true;
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(int code) {
        super(code, Code.UN_AUTHENTICATION.getDescription());
        this.code = code;
        super.ifDefaultMessage = true;
    }

    @Deprecated
    public AuthenticationException(String message, int code) {
        super(code, message);
        this.code = code;
    }

    public AuthenticationException(int code, String message) {
        super(code, message);
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
