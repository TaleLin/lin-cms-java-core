package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 资源不存在异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class NotFoundException extends HttpException {

    private static final long serialVersionUID = 3147792856922208240L;

    private int code = Code.NOT_FOUND.getCode();

    private int httpCode = HttpStatus.NOT_FOUND.value();

    public NotFoundException() {
        super(Code.NOT_FOUND.getDescription(), Code.NOT_FOUND.getCode());
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public NotFoundException(int code) {
        super(Code.NOT_FOUND.getDescription(), code);
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
