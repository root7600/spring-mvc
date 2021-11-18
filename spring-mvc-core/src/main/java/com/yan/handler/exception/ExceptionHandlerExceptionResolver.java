package com.yan.handler.exception;

import com.yan.ModelAndView;
import com.yan.handler.InvocableHandlerMethod;
import com.yan.handler.ModelAndViewContainer;
import com.yan.handler.argument.HandlerMethodArgumentResolver;
import com.yan.handler.argument.HandlerMethodArgumentResolverComposite;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 *
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<>();
    private ConversionService conversionService;
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        InvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        try {
            Throwable cause = ex.getCause();
            if (Objects.nonNull(cause)) {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, cause);
            } else {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, ex);
            }
        } catch (Exception e) {
           e.printStackTrace();
            return null;
        }

        if (mavContainer.isRequestHandled()) {
            return null;
        }

        ModelAndView mav = new ModelAndView();
        mav.setStatus(mavContainer.getStatus());
        mav.setModel(mavContainer.getModel());
        mav.setView(mavContainer.getView());
        return mav;
    }
    private InvocableHandlerMethod getExceptionHandlerMethod(Exception exception) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean advice = entry.getKey();
            ExceptionHandlerMethodResolver resolver = entry.getValue();
            Method method = resolver.resolveMethod(exception);
            if (method != null) {
                return new InvocableHandlerMethod(advice.getBean(),
                        method,
                        this.argumentResolvers,
                        this.returnValueHandlers,
                        this.conversionService);
            }
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
