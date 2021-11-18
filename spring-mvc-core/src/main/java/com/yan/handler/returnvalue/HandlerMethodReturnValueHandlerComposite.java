package com.yan.handler.returnvalue;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler{

    private List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(CollectionUtils.isEmpty(returnValueHandlers)){
            return;
        }

        returnValueHandlers.forEach(handlerMethodReturnValueHandler -> {
            if(handlerMethodReturnValueHandler.supportsReturnType(returnType)){
                try {
                    handlerMethodReturnValueHandler.handleReturnValue(returnValue,returnType,mavContainer,request,response);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clear() {
        this.returnValueHandlers.clear();
    }

    public void addReturnValueHandler(List<HandlerMethodReturnValueHandler> handlers) {
        this.returnValueHandlers.addAll(handlers);
    }

    public void addReturnValueHandler(HandlerMethodReturnValueHandler handler) {
        this.returnValueHandlers.add(handler);
    }
}
