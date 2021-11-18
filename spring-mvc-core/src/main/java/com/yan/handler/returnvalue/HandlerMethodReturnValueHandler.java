package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * supportsReturnType: 同参数解析器一样，判断处理器是否支持该返回值的类型
 * handleReturnValue: returnValue(Handler执行之后的返回值)；
 * 该方法还需要传入HttpServletResponse对象， 是因为可能会在处理其中直接处理完整个请求，比如@ResponseBody
 */
public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(MethodParameter returnType);

    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer mavContainer,
                           HttpServletRequest request, HttpServletResponse response) throws Exception;
}
