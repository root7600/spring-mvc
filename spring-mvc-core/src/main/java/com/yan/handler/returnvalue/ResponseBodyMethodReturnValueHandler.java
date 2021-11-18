package com.yan.handler.returnvalue;

import com.alibaba.fastjson.JSON;
import com.yan.annotation.ResponseBody;
import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    /**
     * mavContainer.setRequestHandled(true);标记出当前请求已经处理完成，后续的渲染无需在执行
     * 使用fastJson把返回值转换成JSON字符串，在使用response输出给前端
     * @param returnValue
     * @param returnType
     * @param mavContainer
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //标记本次请求已经处理完成
        mavContainer.setRequestHandled(true);

        outPutMessage(response, JSON.toJSONString(returnValue));
    }

    private void outPutMessage(HttpServletResponse response, String message) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.write(message);
        }
    }
}
