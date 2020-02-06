package com.lin.cms.autoconfigure.exception;

import com.lin.cms.autoconfigure.beans.Code;
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

    public RefreshFailedException(String message) {
        super(message);
    }

    public RefreshFailedException() {
        super(Code.REFRESH_FAILED.getDescription());
    }

    public RefreshFailedException(int code) {
        super(Code.REFRESH_FAILED.getDescription());
        this.code = code;
    }

    public RefreshFailedException(String message, int code) {
        super(message);
        this.code = code;
    }
}
