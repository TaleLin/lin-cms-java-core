package io.github.talelin.autoconfigure.interfaces;

import io.github.talelin.autoconfigure.bean.MetaInfo;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限认证接口
 *
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public interface AuthorizeVerifyResolver {


    /**
     * 处理 LoginRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleLogin(HttpServletRequest request, HttpServletResponse response, MetaInfo meta);

    /**
     * 处理 GroupRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleGroup(HttpServletRequest request, HttpServletResponse response, MetaInfo meta);

    /**
     * 处理 AdminRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleAdmin(HttpServletRequest request, HttpServletResponse response, MetaInfo meta);

    /**
     * 处理 RefreshRequired的情况
     *
     * @param request  请求
     * @param response 响应
     * @param meta     路由信息
     * @return 是否成功
     */
    boolean handleRefresh(HttpServletRequest request, HttpServletResponse response, MetaInfo meta);

    /**
     * 处理 当前的handler 不是 HandlerMethod 的情况
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 是否成功
     */
    boolean handleNotHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * 后置处理
     *
     * @param request      请求
     * @param response     响应
     * @param handler      处理器
     * @param modelAndView 视图
     */
    default void handlePostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    /**
     * 相应完成后处理
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @param ex       异常
     */
    default void handleAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
