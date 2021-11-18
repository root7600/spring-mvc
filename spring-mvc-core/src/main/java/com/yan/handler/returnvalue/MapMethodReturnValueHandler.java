package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Map.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof Map) {
            mavContainer.getModel().addAllAttributes((Map) returnValue);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
