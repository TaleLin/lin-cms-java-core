package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 刷新令牌失败异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class RefreshFailedException extends HttpException {

    private static final long serialVersionUID = -8626126405157905110L;

    protected int code = Code.REFRESH_FAILED.getCode();

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

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }
}
