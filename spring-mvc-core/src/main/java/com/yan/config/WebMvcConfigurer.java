package com.yan.config;

import com.yan.handler.argument.HandlerMethodArgumentResolver;
import com.yan.handler.interceptor.InterceptorRegistry;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.yan.view.View;
import com.yan.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistry;

import java.util.List;

public interface WebMvcConfigurer {

    //参数解析器的扩展点
    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    }

    //返回值解析器
    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    }

    //拦截器暴露对外的扩展点
    default void addInterceptors(InterceptorRegistry registry) {
    }

    //数据转换格式化暴露对外的扩展点
    default void addFormatters(FormatterRegistry registry) {
    }

    //视图的扩展点
    default void addDefaultViews(List<View> views) {
    }

    //视图解析器的扩展点
    default void addViewResolvers(List<ViewResolver> viewResolvers) {
    }
}
