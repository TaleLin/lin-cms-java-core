package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 禁止操作异常
 */
public class ForbiddenException extends HttpException {

    @Getter
    protected int code = Code.FORBIDDEN.getCode();

    @Getter
    protected int httpCode = HttpStatus.FORBIDDEN.value();

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ForbiddenException(int code) {
        super(Code.FORBIDDEN.getDescription());
        this.code = code;
    }

    public ForbiddenException() {
        super(Code.FORBIDDEN.getDescription());
    }
}
