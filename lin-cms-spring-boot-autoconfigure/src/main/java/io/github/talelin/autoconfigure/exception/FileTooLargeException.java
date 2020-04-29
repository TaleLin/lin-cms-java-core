package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件太大异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class FileTooLargeException extends HttpException {

    private static final long serialVersionUID = -3845807826439492213L;

    protected int code = Code.FILE_TOO_LARGE.getCode();

    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();

    public FileTooLargeException() {
        super(Code.FILE_TOO_LARGE.getDescription(), Code.FILE_TOO_LARGE.getCode());
    }

    public FileTooLargeException(String message) {
        super(message);
    }

    public FileTooLargeException(int code) {
        super(Code.FILE_TOO_LARGE.getDescription(), code);
        this.code = code;
    }

    public FileTooLargeException(String message, int code) {
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
