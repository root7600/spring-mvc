package com.yan;

import com.yan.handler.HandlerMapping;
import com.yan.handler.adapter.HandlerAdapter;
import com.yan.handler.exception.HandlerExceptionResolver;
import com.yan.handler.mapping.HandlerExecutionChain;
import com.yan.utils.RequestContextHolder;
import com.yan.view.View;
import com.yan.view.resolver.ViewResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class DispatcherServlet extends HttpServlet implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;
    private Collection<HandlerExceptionResolver> handlerExceptionResolvers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    /**
     * 初始化部分，当Servlet在第一次初始化的时候会调用 init方法，在该方法里对诸如 handlerMapping，ViewResolver 等进行初始化，
     * 代码如下：
     */
    @Override
    public void init() {
        this.handlerMapping = this.applicationContext.getBean(HandlerMapping.class);
        this.handlerAdapter = this.applicationContext.getBean(HandlerAdapter.class);
        this.viewResolver = this.applicationContext.getBean(ViewResolver.class);
        this.handlerExceptionResolvers =
                this.applicationContext.getBeansOfType(HandlerExceptionResolver.class).values();
    }

    /**
     * 对HTTP请求进行响应，作为一个Servlet，当请求到达时Web容器会调用其service方法;
     * 通过RequestContextHolder在线程变量中设置request，然后调用doDispatch完成请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestContextHolder.setRequest(request);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RequestContextHolder.resetRequest();
        }
    }

    /**
     * 在doDispatch方法中的执行逻辑
     * 首先通过handlerMapping获取到处理本次请求的HandlerExecutionChain
     * 执行拦截器的前置方法
     * 通过handlerAdapter执行handler返回ModelAndView
     * 执行拦截器的后置方法
     * 处理返回的结果processDispatchResult
     * 在处理完成请求后调用executionChain.triggerAfterCompletion(request, response, dispatchException);，完成拦截器的afterCompletion方法调用
     * @param request
     * @param response
     * @throws Exception
     */
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Exception dispatchException = null;
        HandlerExecutionChain executionChain = null;
        try {
            ModelAndView mv = null;
            try {
                executionChain = this.handlerMapping.getHandler(request);

                if (!executionChain.applyPreHandle(request, response)) {
                    return;
                }
                // Actually invoke the handler.
                mv = handlerAdapter.handle(request, response, executionChain.getHandler());

                executionChain.applyPostHandle(request, response, mv);
            } catch (Exception e) {
                dispatchException = e;
            }
            processDispatchResult(request, response, mv, dispatchException);
        } catch (Exception ex) {
            dispatchException = ex;
            throw ex;
        } finally {
            if (Objects.nonNull(executionChain)) {
                executionChain.triggerAfterCompletion(request, response, dispatchException);
            }
        }

    }

    /**
     * processDispatchResult方法中又分为两个逻辑，如果是正常的返回ModelAndView，
     * 那么就执行render方法，如果在执行的过程中抛出了任何异常，那么就会执行processHandlerException，方便做全局异常处理
     * @param request
     * @param response
     * @param mv
     * @param ex
     * @throws Exception
     */
    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       ModelAndView mv, Exception ex) throws Exception {
        if (Objects.nonNull(ex)) {
            //error ModelAndView
            mv = processHandlerException(request, response, ex);
        }

        if (Objects.nonNull(mv)) {
            render(mv, request, response);
            return;
        }
    }

    //出现异常后的ModelAndView
    private ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        if (CollectionUtils.isEmpty(this.handlerExceptionResolvers)) {
            throw ex;
        }
        for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
            ModelAndView exMv = resolver.resolveException(request, response, ex);
            if (exMv != null) {
                return exMv;
            }
        }
        //未找到对应的异常处理器，就继续抛出异常
        throw ex;
    }

    private void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

        View view;
        String viewName = mv.getViewName();
        if (!StringUtils.isEmpty(viewName)) {
            view = this.viewResolver.resolveViewName(viewName);
        } else {
            view = (View) mv.getView();
        }

        if (mv.getStatus() != null) {
            response.setStatus(mv.getStatus().getValue());
        }
        view.render(mv.getModel().asMap(), request, response);
    }


}
