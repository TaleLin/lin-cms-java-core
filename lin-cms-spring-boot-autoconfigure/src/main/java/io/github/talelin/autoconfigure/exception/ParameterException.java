package io.github.talelin.autoconfigure.exception;

import io.github.talelin.autoconfigure.bean.Code;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数错误异常
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * @author colorful@TaleLin
 */
public class ParameterException extends HttpException {

    private static final long serialVersionUID = 3437368397374839983L;

    protected int httpCode = HttpStatus.BAD_REQUEST.value();

    protected int code = Code.PARAMETER_ERROR.getCode();

    private Map<String, Object> errors = new HashMap<>();

    public ParameterException() {
        super(Code.PARAMETER_ERROR.getCode(), Code.PARAMETER_ERROR.getDescription());
        super.ifDefaultMessage = true;
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(int code) {
        super(code, Code.PARAMETER_ERROR.getDescription());
        this.code = code;
        super.ifDefaultMessage=true;
    }

    @Deprecated
    public ParameterException(String message, int code) {
        super(message, code);
        this.code = code;
    }

    public ParameterException(int code, String message) {
        super(code, message);
        this.code = code;
    }

    public ParameterException(Map<String, Object> errors) {
        this.errors = errors;
    }

    public ParameterException addError(String key, Object val) {
        this.errors.put(key, val);
        return this;
    }

    @Override
    public String getMessage() {
        if (errors.isEmpty()) {
            return super.getMessage();
        }
        return errors.toString();
    }

    @Override
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public int getCode() {
        return code;
    }
}
