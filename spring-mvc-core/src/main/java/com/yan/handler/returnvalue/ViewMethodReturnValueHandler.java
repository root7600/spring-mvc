package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import com.yan.view.View;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return View.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof View) {
            View view = (View) returnValue;
            mavContainer.setView(view);
        } else if (returnValue != null) {
            // should not happen
            throw new UnsupportedOperationException("Unexpected return type: " +
                    returnType.getParameterType().getName() + " in method: " + returnType.getMethod());
        }
    }
}
