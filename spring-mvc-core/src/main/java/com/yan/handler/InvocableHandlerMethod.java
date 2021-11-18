package com.yan.handler;

import com.yan.handler.argument.HandlerMethodArgumentResolverComposite;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class InvocableHandlerMethod extends HandlerMethod{

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private HandlerMethodArgumentResolverComposite argumentResolver;
    private HandlerMethodReturnValueHandlerComposite returnValueHandler;
    private ConversionService conversionService;

    public InvocableHandlerMethod(Object bean, Method method, ParameterNameDiscoverer parameterNameDiscoverer,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }
    /**
     * 调用handler
     *
     * @param request
     * @param mavContainer
     * @throws Exception
     */
    public void invokeAndHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndViewContainer mavContainer,
                                Object... providedArgs) throws Exception {

        List<Object> args = this.getMethodArgumentValues(request, response, mavContainer);
        Object resultValue = doInvoke(args);
        //返回为空
        if (Objects.isNull(resultValue)) {
            if (response.isCommitted()) {
                mavContainer.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }

        mavContainer.setRequestHandled(false);
        Assert.state(this.returnValueHandler != null, "No return value handler");

        MethodParameter returnType = new MethodParameter(this.getMethod(), -1);  //-1表示方法的返回值
        this.returnValueHandler.handleReturnValue(resultValue, returnType, mavContainer, request, response);

    }

    private Object doInvoke(List<Object> args) throws InvocationTargetException, IllegalAccessException {
        return this.getMethod().invoke(this.getBean(), args.toArray());
    }

    private List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 ModelAndViewContainer mavContainer) throws Exception {
        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver can not null");

        List<MethodParameter> parameters = this.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter parameter : parameters) {
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            args.add(argumentResolver.resolveArgument(parameter, request, response, mavContainer, conversionService));
        }
        return args;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
