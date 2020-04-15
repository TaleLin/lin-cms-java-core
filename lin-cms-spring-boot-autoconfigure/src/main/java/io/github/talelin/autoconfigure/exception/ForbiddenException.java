package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 禁止操作异常
 *
 * @author pedro@TaleLin
 */
public class ForbiddenException extends HttpException {

    @Getter
    protected int code = Code.FORBIDDEN.getCode();

    @Getter
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

}
