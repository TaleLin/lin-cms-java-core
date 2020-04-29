package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import org.springframework.http.HttpStatus;

/**
 * 更新相应
 *
 * @author colorful@TaleLin
 * @author Juzi@TaleLin
 */
public class Updated implements BaseResponse {

    protected String message = Code.UPDATED.getDescription();

    protected int code = Code.UPDATED.getCode();

    protected int httpCode = HttpStatus.OK.value();


    public Updated(String message) {
        this.message = message;
    }

    public Updated() {
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
