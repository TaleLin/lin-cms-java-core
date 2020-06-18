package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 失败异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class FailedException extends HttpException {

    private static final long serialVersionUID = -661265124636854465L;

    protected int code = Code.FAIL.getCode();

    protected int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public FailedException() {
        super(Code.FAIL.getCode(), Code.FAIL.getDescription());
        super.ifDefaultMessage=true;
    }

    public FailedException(String message) {
        super(message);
    }

    public FailedException(int code) {
        super(code, Code.FAIL.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public FailedException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public FailedException(int code, String message) {
        super(code, message);
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
