package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 字段重复
 */
public class RepeatException extends HttpException {

    @Getter
    protected int code = Code.REPEAT.getCode();

    @Getter
    protected int httpCode = HttpStatus.BAD_REQUEST.value();

    public RepeatException(String message) {
        super(message);
    }

    public RepeatException() {
        super(Code.REPEAT.getDescription());
    }

    public RepeatException(int code) {
        super(Code.REPEAT.getDescription());
        this.code = code;
    }

    public RepeatException(String message, int code) {
        super(message);
        this.code = code;
    }
}
