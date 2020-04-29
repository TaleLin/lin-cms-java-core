package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件太多异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class FileTooManyException extends HttpException {

    private static final long serialVersionUID = -3189291002817434249L;

    protected int code = Code.FILE_TOO_MANY.getCode();

    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();


    public FileTooManyException() {
        super(Code.FILE_TOO_MANY.getDescription(), Code.FILE_TOO_MANY.getCode());
    }

    public FileTooManyException(String message) {
        super(message);
    }

    public FileTooManyException(int code) {
        super(Code.FILE_TOO_MANY.getDescription(), code);
        this.code = code;
    }


    public FileTooManyException(String message, int code) {
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
