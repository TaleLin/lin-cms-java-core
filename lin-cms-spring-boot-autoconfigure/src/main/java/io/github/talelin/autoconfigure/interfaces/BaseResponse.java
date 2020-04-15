package io.github.talelin.autoconfigure.interfaces;

/**
 * 基础的返回接口
 * 必须实现以下三个方法
 *
 * @author pedro@TaleLin
 */
public interface BaseResponse {

    /**
     * 返回的信息
     *
     * @return 返回的信息
     */
    String getMessage();

    /**
     * http 状态码
     *
     * @return http 状态码
     */
    int getHttpCode();

    /**
     * 错误码
     *
     * @return 错误码
     */
    int getCode();
}
