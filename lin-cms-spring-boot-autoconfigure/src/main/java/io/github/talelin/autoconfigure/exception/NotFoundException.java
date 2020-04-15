package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 资源不存在异常
 *
 * @author pedro@TaleLin
 */
public class NotFoundException extends HttpException {

    @Getter
    private int code = Code.NOT_FOUND.getCode();

    @Getter
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

}
