package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
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

    public MethodNotAllowedException() {
        super(Code.METHOD_NOT_ALLOWED.getDescription(), Code.METHOD_NOT_ALLOWED.getCode());
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(int code) {
        super(Code.METHOD_NOT_ALLOWED.getDescription(), code);
        this.code = code;
    }

    public MethodNotAllowedException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
