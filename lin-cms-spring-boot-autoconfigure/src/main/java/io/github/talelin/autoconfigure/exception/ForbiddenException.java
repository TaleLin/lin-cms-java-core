package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 禁止操作异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class ForbiddenException extends HttpException {

    private static final long serialVersionUID = 865571132800721223L;

    protected int code = Code.FORBIDDEN.getCode();

    protected int httpCode = HttpStatus.FORBIDDEN.value();


    public ForbiddenException() {
        super(Code.FORBIDDEN.getDescription(), Code.FORBIDDEN.getCode());
    }

    public ForbiddenException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(int code) {
        super(Code.FORBIDDEN.getDescription(), code);
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
