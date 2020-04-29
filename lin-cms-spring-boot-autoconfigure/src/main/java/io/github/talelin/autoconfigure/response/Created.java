package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import org.springframework.http.HttpStatus;

/**
 * 新建响应
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public class Created implements BaseResponse {

    protected String message = Code.CREATED.getDescription();

    protected int code = Code.CREATED.getCode();

    protected int httpCode = HttpStatus.CREATED.value();


    public Created(String message) {
        this.message = message;
    }

    public Created() {
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
