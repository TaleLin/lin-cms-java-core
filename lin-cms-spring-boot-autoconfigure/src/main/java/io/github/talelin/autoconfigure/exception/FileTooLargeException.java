package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 文件太大异常
 */
public class FileTooLargeException extends HttpException {

    @Getter
    protected int code = Code.FILE_TOO_LARGE.getCode();

    @Getter
    protected int httpCode = HttpStatus.PAYLOAD_TOO_LARGE.value();


    public FileTooLargeException(String message) {
        super(message);
    }

    public FileTooLargeException() {
        super(Code.FILE_TOO_LARGE.getDescription());
    }

    public FileTooLargeException(int code) {
        super(Code.FILE_TOO_LARGE.getDescription());
        this.code = code;
    }

    public FileTooLargeException(String message, int code) {
        super(message);
        this.code = code;
    }
}
