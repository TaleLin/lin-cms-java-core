package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 文件太多异常
 */
public class FileTooManyException extends HttpException {

    @Getter
    protected int code = Code.FILE_TOO_MANY.getCode();

    @Getter
    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();


    public FileTooManyException(String message) {
        super(message);
    }

    public FileTooManyException() {
        super(Code.FILE_TOO_MANY.getDescription());
    }

    public FileTooManyException(int code) {
        super(Code.FILE_TOO_MANY.getDescription());
        this.code = code;
    }


    public FileTooManyException(String message, int code) {
        super(message);
        this.code = code;
    }
}
