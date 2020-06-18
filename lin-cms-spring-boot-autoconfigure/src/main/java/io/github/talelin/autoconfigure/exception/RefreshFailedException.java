package io.github.talelin.autoconfigure.exception;


import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

/**
 * 刷新令牌失败异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class RefreshFailedException extends HttpException {

    private static final long serialVersionUID = -8626126405157905110L;

    protected int code = Code.REFRESH_FAILED.getCode();

    protected int httpCode = HttpStatus.UNAUTHORIZED.value();

    public RefreshFailedException() {
        super(Code.REFRESH_FAILED.getCode(), Code.REFRESH_FAILED.getDescription());
        super.ifDefaultMessage=true;
    }

    public RefreshFailedException(String message) {
        super(message);
    }

    public RefreshFailedException(int code) {
        super(code, Code.REFRESH_FAILED.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public RefreshFailedException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public RefreshFailedException(int code, String message) {
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
