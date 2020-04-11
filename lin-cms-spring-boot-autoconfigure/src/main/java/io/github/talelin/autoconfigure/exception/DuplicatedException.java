package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 字段重复
 */
public class DuplicatedException extends HttpException {

    @Getter
    protected int code = Code.DUPLICATED.getCode();

    @Getter
    protected int httpCode = HttpStatus.BAD_REQUEST.value();

    public DuplicatedException(String message) {
        super(message);
    }

    public DuplicatedException() {
        super(Code.DUPLICATED.getDescription());
    }

    public DuplicatedException(int code) {
        super(Code.DUPLICATED.getDescription(), Code.DUPLICATED.getCode());
        this.code = code;
    }

    public DuplicatedException(String message, int code) {
        super(message, Code.DUPLICATED.getCode());
        this.code = code;
    }
}
