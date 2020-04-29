package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import org.springframework.http.HttpStatus;

/**
 * 删除相应
 *
 * @author colorful@TaleLin
 * @author Juzi@TaleLin
 */
public class Deleted implements BaseResponse {

    protected String message = Code.UPDATED.getDescription();

    protected int code = Code.DELETED.getCode();

    protected int httpCode = HttpStatus.OK.value();


    public Deleted(String message) {
        this.message = message;
    }

    public Deleted() {
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
