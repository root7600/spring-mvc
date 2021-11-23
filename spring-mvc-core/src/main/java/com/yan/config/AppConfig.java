package com.yan.config;

import com.yan.DispatcherServlet;
import com.yan.annotation.EnableWebMvc;
import com.yan.handler.HandlerMapping;
import com.yan.handler.adapter.HandlerAdapter;
import com.yan.handler.adapter.RequestMappingHandlerAdapter;
import com.yan.handler.exception.ExceptionHandlerExceptionResolver;
import com.yan.handler.exception.HandlerExceptionResolver;
import com.yan.handler.interceptor.InterceptorRegistry;
import com.yan.handler.mapping.RequestMappingHandlerMapping;
import com.yan.view.resolver.ContentNegotiatingViewResolver;
import com.yan.view.resolver.InternalResourceViewResolver;
import com.yan.view.resolver.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.util.Collections;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
@Configuration
@ComponentScan(basePackages = "com.yan")
@EnableWebMvc
public class AppConfig {

/*    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor2)
                .addIncludePatterns("/in_test2", "/in_test3");

        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
        return mapping;
    }*/

/*    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }
    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);
        return conversionService;
    }
    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        return negotiatingViewResolver;
    }*/
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

/*    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(ConversionService conversionService) {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setConversionService(conversionService);
        return resolver;
    }*/
}
