package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 请求过多
 */
public class RequestLimitException extends HttpException {

    @Getter
    protected int code = Code.REQUEST_LIMIT.getCode();

    @Getter
    protected int httpCode = HttpStatus.TOO_MANY_REQUESTS.value();

    public RequestLimitException() {
        super(Code.REQUEST_LIMIT.getDescription(), Code.REQUEST_LIMIT.getCode());
    }

    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException(int code) {
        super(Code.REQUEST_LIMIT.getDescription());
        this.code = code;
    }

    public RequestLimitException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
