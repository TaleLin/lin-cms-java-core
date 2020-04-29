package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 失败异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class FailedException extends HttpException {

    private static final long serialVersionUID = -661265124636854465L;

    protected int code = Code.FAIL.getCode();

    protected int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public FailedException() {
        super(Code.FAIL.getDescription(), Code.FAIL.getCode());
    }

    public FailedException(String message) {
        super(message);
    }

    public FailedException(int code) {
        super(Code.FAIL.getDescription(), code);
        this.code = code;
    }

    public FailedException(String message, int code) {
        super(message, code);
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
