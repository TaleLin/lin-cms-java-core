package com.lin.cms.autoconfigure.response;

import com.lin.cms.autoconfigure.beans.Code;
import com.lin.cms.autoconfigure.interfaces.BaseResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
