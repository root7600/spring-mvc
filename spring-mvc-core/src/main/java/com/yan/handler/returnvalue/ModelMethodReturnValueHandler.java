package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Model.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue == null) {
            return;
        } else if (returnValue instanceof Model) {
            mavContainer.getModel().addAllAttributes(((Model) returnValue).asMap());
        } else {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
