package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import org.springframework.http.HttpStatus;

/**
 * HttpException 异常类
 * 含异常信息 message
 * http状态码 httpCode
 * 错误码 code
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class HttpException extends RuntimeException implements BaseResponse {

    private static final long serialVersionUID = 2359767895161832954L;

    protected int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    protected int code = Code.INTERNAL_SERVER_ERROR.getCode();

    protected boolean messageOnly = false;

    public HttpException() {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
    }

    public HttpException(String message) {
        super(message);
        this.messageOnly = true;
    }

    public HttpException(int code) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.code = code;
    }

    public HttpException(int code, int httpCode) {
        super(Code.INTERNAL_SERVER_ERROR.getDescription());
        this.httpCode = httpCode;
        this.code = code;
    }

    @Deprecated
    public HttpException(String message, int code) {
        super(message);
        this.code = code;
    }

    @Deprecated
    public HttpException(String message, int code, int httpCode) {
        super(message);
        this.httpCode = httpCode;
        this.code = code;
    }

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HttpException(int code, String message, int httpCode) {
        super(message);
        this.code = code;
        this.httpCode = httpCode;
    }

    public HttpException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public HttpException(Throwable cause, int code, int httpCode) {
        super(cause);
        this.code = code;
        this.httpCode = httpCode;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * for better performance
     *
     * @return Throwable
     */
    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public int getHttpCode() {
        return this.httpCode;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public boolean isMessageOnly() {
        return messageOnly;
    }

}
