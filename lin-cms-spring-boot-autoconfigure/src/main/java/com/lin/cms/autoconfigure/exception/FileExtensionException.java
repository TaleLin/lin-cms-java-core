package com.lin.cms.autoconfigure.exception;

import com.lin.cms.autoconfigure.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class FileExtensionException extends HttpException {

    @Getter
    protected int code = Code.FILE_EXTENSION.getCode();

    @Getter
    protected int httpCode = HttpStatus.NOT_ACCEPTABLE.value();

    public FileExtensionException(String message) {
        super(message);
    }

    public FileExtensionException() {
        super(Code.FILE_EXTENSION.getDescription());
    }

    public FileExtensionException(int code) {
        super(Code.FILE_EXTENSION.getDescription());
        this.code = code;
    }

    public FileExtensionException(String message, int code) {
        super(message);
        this.code = code;
    }
}
