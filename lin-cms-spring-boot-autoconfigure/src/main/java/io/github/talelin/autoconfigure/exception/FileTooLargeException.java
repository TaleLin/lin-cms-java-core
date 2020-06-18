package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件太大异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class FileTooLargeException extends HttpException {

    private static final long serialVersionUID = -3845807826439492213L;

    protected int code = Code.FILE_TOO_LARGE.getCode();

    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();

    public FileTooLargeException() {
        super(Code.FILE_TOO_LARGE.getCode(), Code.FILE_TOO_LARGE.getDescription());
        super.ifDefaultMessage=true;
    }

    public FileTooLargeException(String message) {
        super(message);
    }

    public FileTooLargeException(int code) {
        super(code, Code.FILE_TOO_LARGE.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public FileTooLargeException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public FileTooLargeException(int code, String message) {
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
