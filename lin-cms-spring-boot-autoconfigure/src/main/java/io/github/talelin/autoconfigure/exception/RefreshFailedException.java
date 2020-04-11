package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 刷新令牌失败
 */
public class RefreshFailedException extends HttpException {

    @Getter
    protected int code = Code.REFRESH_FAILED.getCode();

    @Getter
    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public RefreshFailedException() {
        super(Code.REFRESH_FAILED.getDescription(), Code.REFRESH_FAILED.getCode());
    }

    public RefreshFailedException(String message) {
        super(message);
    }

    public RefreshFailedException(int code) {
        super(Code.REFRESH_FAILED.getDescription(), code);
        this.code = code;
    }

    public RefreshFailedException(String message, int code) {
        super(message, code);
        this.code = code;
    }
}
