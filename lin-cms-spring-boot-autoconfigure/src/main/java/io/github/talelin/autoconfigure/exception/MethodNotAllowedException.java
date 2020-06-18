package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 方法不允许异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class MethodNotAllowedException extends HttpException {

    private static final long serialVersionUID = 1223018751542741014L;

    protected int code = Code.METHOD_NOT_ALLOWED.getCode();

    protected int httpCode = HttpStatus.METHOD_NOT_ALLOWED.value();

    public MethodNotAllowedException() {
        super(Code.METHOD_NOT_ALLOWED.getCode(), Code.METHOD_NOT_ALLOWED.getDescription());
        super.ifDefaultMessage=true;
    }

    public MethodNotAllowedException(String message) {
        super(message);
    }

    public MethodNotAllowedException(int code) {
        super(code, Code.METHOD_NOT_ALLOWED.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public MethodNotAllowedException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public MethodNotAllowedException(int code, String message) {
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
