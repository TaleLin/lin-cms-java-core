package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 请求过多异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class RequestLimitException extends HttpException {

    private static final long serialVersionUID = 1909144765577512625L;

    protected int code = Code.REQUEST_LIMIT.getCode();

    protected int httpCode = HttpStatus.TOO_MANY_REQUESTS.value();

    public RequestLimitException() {
        super(Code.REQUEST_LIMIT.getCode(), Code.REQUEST_LIMIT.getDescription());
        super.ifDefaultMessage=true;
    }

    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException(int code) {
        super(code, Code.REQUEST_LIMIT.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public RequestLimitException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public RequestLimitException(int code, String message) {
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
