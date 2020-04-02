package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 新建响应
 */
public class Created implements BaseResponse {

    @Getter
    protected String message = Code.CREATED.getDescription();

    @Getter
    protected int code = Code.CREATED.getCode();

    @Getter
    protected int httpCode = HttpStatus.CREATED.value();


    public Created(String message) {
        this.message = message;
    }

    public Created() {
    }
}
