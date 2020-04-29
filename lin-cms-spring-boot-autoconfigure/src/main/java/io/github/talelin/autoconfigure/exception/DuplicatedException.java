package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 字段重复
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class DuplicatedException extends HttpException {

    private static final long serialVersionUID = -6459429029468050951L;

    protected int code = Code.DUPLICATED.getCode();

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

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }
}
