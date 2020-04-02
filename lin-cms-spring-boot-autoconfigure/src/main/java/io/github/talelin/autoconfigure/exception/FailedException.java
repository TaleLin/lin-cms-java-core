package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 失败异常
 */
public class FailedException extends HttpException {

    @Getter
    protected int code = Code.FAIL.getCode();

    @Getter
    protected int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public FailedException() {
        super(Code.FAIL.getDescription());
    }

    public FailedException(String message) {
        super(message);
    }

    public FailedException(int code) {
        super(Code.FAIL.getDescription());
        this.code = code;
    }

    public FailedException(String message, int code) {
        super(message);
        this.code = code;
    }
}
