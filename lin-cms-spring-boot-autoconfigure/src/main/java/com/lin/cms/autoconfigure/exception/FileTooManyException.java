package com.lin.cms.autoconfigure.exception;

import com.lin.cms.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
