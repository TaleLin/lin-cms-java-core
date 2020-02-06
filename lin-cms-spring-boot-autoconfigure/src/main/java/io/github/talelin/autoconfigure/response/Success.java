package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.beans.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 成功响应
 */
public class Success implements BaseResponse {

    @Getter
    protected String message = Code.SUCCESS.getDescription();

    @Getter
    protected int code = Code.SUCCESS.getCode();

    @Getter
    protected int httpCode = HttpStatus.OK.value();


    public Success(String message) {
        this.message = message;
    }

    public Success() {
    }
}
