package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        return CharSequence.class.isAssignableFrom(paramType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof CharSequence) {
            String viewName = returnValue.toString();
            mavContainer.setViewName(viewName);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
