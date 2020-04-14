package io.github.talelin.autoconfigure.response;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.talelin.autoconfigure.interfaces.BaseResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 删除相应
 *
 * @author colorful@TaleLin
 */
public class Deleted implements BaseResponse {

    @Getter
    protected String message = Code.UPDATED.getDescription();

    @Getter
    protected int code = Code.DELETED.getCode();

    @Getter
    protected int httpCode = HttpStatus.OK.value();


    public Deleted(String message) {
        this.message = message;
    }

    public Deleted() {
    }

}
