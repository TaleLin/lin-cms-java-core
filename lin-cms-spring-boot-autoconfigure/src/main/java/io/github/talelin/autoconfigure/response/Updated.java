package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 更新相应
 *
 * @author colorful@TaleLin
 */
public class Updated implements BaseResponse {

    @Getter
    protected String message = Code.UPDATED.getDescription();

    @Getter
    protected int code = Code.UPDATED.getCode();

    @Getter
    protected int httpCode = HttpStatus.OK.value();


    public Updated(String message) {
        this.message = message;
    }

    public Updated() {
    }

}
