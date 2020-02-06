package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 方法不允许异常
 */
public class MethodNotAllowedException extends HttpException {

    @Getter
    protected int code = Code.METHOD_NOT_ALLOWED.getCode();

    @Getter
    protected int httpCode = HttpStatus.METHOD_NOT_ALLOWED.value();

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException() {
        super(Code.METHOD_NOT_ALLOWED.getDescription());
    }

    public MethodNotAllowedException(int code) {
        super(Code.METHOD_NOT_ALLOWED.getDescription());
        this.code = code;
    }

    public MethodNotAllowedException(String message, int code) {
        super(message);
        this.code = code;
    }
}
