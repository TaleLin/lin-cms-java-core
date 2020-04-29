package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import org.springframework.http.HttpStatus;

/**
 * 成功响应
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class Success implements BaseResponse {

    protected String message = Code.SUCCESS.getDescription();

    protected int code = Code.SUCCESS.getCode();

    protected int httpCode = HttpStatus.OK.value();


    public Success(String message) {
        this.message = message;
    }

    public Success() {
    }

    @Override
    public String getMessage() {
        return message;
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
