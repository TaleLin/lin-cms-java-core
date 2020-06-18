package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件太多异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class FileTooManyException extends HttpException {

    private static final long serialVersionUID = -3189291002817434249L;

    protected int code = Code.FILE_TOO_MANY.getCode();

    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();


    public FileTooManyException() {
        super(Code.FILE_TOO_MANY.getCode(), Code.FILE_TOO_MANY.getDescription());
        super.ifDefaultMessage=true;
    }

    public FileTooManyException(String message) {
        super(message);
    }

    public FileTooManyException(int code) {
        super(code, Code.FILE_TOO_MANY.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }


    @Deprecated
    public FileTooManyException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public FileTooManyException(int code, String message) {
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
