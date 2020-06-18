package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 文件扩展异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class FileExtensionException extends HttpException {

    private static final long serialVersionUID = 7328828500425088567L;

    protected int code = Code.FILE_EXTENSION.getCode();

    protected int httpCode = HttpStatus.NOT_ACCEPTABLE.value();

    public FileExtensionException() {
        super(Code.FILE_EXTENSION.getCode(), Code.FILE_EXTENSION.getDescription());
        super.ifDefaultMessage=true;
    }

    public FileExtensionException(String message) {
        super(message);
    }


    public FileExtensionException(int code) {
        super(code, Code.FILE_EXTENSION.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public FileExtensionException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public FileExtensionException(int code, String message) {
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
