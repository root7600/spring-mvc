package com.yan.handler.adapter;

import com.yan.ModelAndView;
import com.yan.handler.HandlerMethod;
import com.yan.handler.InvocableHandlerMethod;
import com.yan.handler.ModelAndViewContainer;
import com.yan.handler.argument.HandlerMethodArgumentResolver;
import com.yan.handler.argument.HandlerMethodArgumentResolverComposite;
import com.yan.handler.argument.ModelMethodArgumentResolver;
import com.yan.handler.argument.RequestBodyMethodArgumentResolver;
import com.yan.handler.argument.RequestParamMethodArgumentResolver;
import com.yan.handler.argument.ServletRequestMethodArgumentResolver;
import com.yan.handler.argument.ServletResponseMethodArgumentResolver;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import com.yan.handler.returnvalue.MapMethodReturnValueHandler;
import com.yan.handler.returnvalue.ModelMethodReturnValueHandler;
import com.yan.handler.returnvalue.ResponseBodyMethodReturnValueHandler;
import com.yan.handler.returnvalue.ViewMethodReturnValueHandler;
import com.yan.handler.returnvalue.ViewNameMethodReturnValueHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des RequestMappingHandlerAdapter本身在SpringMVC中占有重要的地位，虽然它只是HandlerAdapter的一种实现，
 * 但是它是使用最多的一个实现类，主要用于将某个请求适配给@RequestMapping类型的Handler处理
 *  考虑到框架的扩展性，所以这里定义了customArgumentResolvers、customReturnValueHandlers两个变量，
 *  如果MVC提供的参数解析器和返回值处理器不满足用户的需求，允许添加自定义的参数解析器和返回值处理器
 *  在RequestMappingHandlerAdapter加入到spring容器之后需要做一些初始化的工作，所以实现了接口InitializingBean，在afterPropertiesSet方法中我们需要把系统默认支持的参数解析器和返回值处理器以及用户自定义的一起添加到系统中。
 * 当DispatcherServlet处理用户请求的时候会调用HandlerAdapter的handle方法，这时候先通过传入HandlerMethod创建之前我们已经开发完成的组件InvocableHandlerMethod，然后调用invokeAndHandle执行控制器的方法
 * 当执行完成控制器的方法，我们需要通过ModelAndViewContainer创建ModelAndView对象返回
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter , InitializingBean {

    private List<HandlerMethodArgumentResolver> customArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        InvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        invocableMethod.invokeAndHandle(request, response, mavContainer);

        return getModelAndView(mavContainer);
    }

    private ModelAndView getModelAndView(ModelAndViewContainer mavContainer) {
        if (mavContainer.isRequestHandled()) {
            //本次请求已经处理完成
            return null;
        }

        ModelAndView mav = new ModelAndView();
        mav.setStatus(mavContainer.getStatus());
        mav.setModel(mavContainer.getModel());
        mav.setView(mavContainer.getView());
        return mav;
    }

    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod,
                this.argumentResolverComposite,
                this.returnValueHandlerComposite,
                this.conversionService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService can not null");
        if (Objects.isNull(argumentResolverComposite)) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            this.argumentResolverComposite.addResolver(resolvers);
        }

        if (Objects.isNull(returnValueHandlerComposite)) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            this.returnValueHandlerComposite.addReturnValueHandler(handlers);
        }
    }

    /**
     * 初始化默认返回值处理器
     *
     * @return
     */
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        handlers.add(new MapMethodReturnValueHandler());
        handlers.add(new ModelMethodReturnValueHandler());
        handlers.add(new ResponseBodyMethodReturnValueHandler());
        handlers.add(new ViewNameMethodReturnValueHandler());
        handlers.add(new ViewMethodReturnValueHandler());

        if (!CollectionUtils.isEmpty(getCustomReturnValueHandlers())) {
            handlers.addAll(getDefaultReturnValueHandlers());
        }

        return handlers;
    }

    /**
     * 初始化默认参数解析器
     *
     * @return
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(getCustomArgumentResolvers())) {
            resolvers.addAll(getCustomArgumentResolvers());
        }

        return resolvers;
    }

    public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers() {
        return customArgumentResolvers;
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers = customArgumentResolvers;
    }

    public HandlerMethodArgumentResolverComposite getArgumentResolverComposite() {
        return argumentResolverComposite;
    }

    public void setArgumentResolverComposite(HandlerMethodArgumentResolverComposite argumentResolverComposite) {
        this.argumentResolverComposite = argumentResolverComposite;
    }

    public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers() {
        return customReturnValueHandlers;
    }

    public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> customReturnValueHandlers) {
        this.customReturnValueHandlers = customReturnValueHandlers;
    }

    public HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        return returnValueHandlerComposite;
    }

    public void setReturnValueHandlerComposite(HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite) {
        this.returnValueHandlerComposite = returnValueHandlerComposite;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
