package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 文件扩展异常
 */
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
