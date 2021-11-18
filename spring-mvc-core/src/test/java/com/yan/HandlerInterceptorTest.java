package com.yan;

import com.alibaba.fastjson.JSON;
import com.yan.config.Test2HandlerInterceptor;
import com.yan.config.TestHandlerInterceptor;
import com.yan.exception.NoHandlerFoundException;
import com.yan.handler.HandlerMethod;
import com.yan.handler.adapter.RequestMappingHandlerAdapter;
import com.yan.handler.argument.HandlerMethodArgumentResolverComposite;
import com.yan.handler.argument.RequestBodyMethodArgumentResolver;
import com.yan.handler.argument.RequestParamMethodArgumentResolver;
import com.yan.handler.argument.ServletRequestMethodArgumentResolver;
import com.yan.handler.interceptor.InterceptorRegistry;
import com.yan.handler.interceptor.MappedInterceptor;
import com.yan.handler.mapping.HandlerExecutionChain;
import com.yan.handler.mapping.RequestMappingHandlerMapping;
import com.yan.view.RedirectView;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class HandlerInterceptorTest extends BaseJunit4Test{
    private InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Test
    public void test() throws Exception {
        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();

        interceptorRegistry.addInterceptor(interceptor)
                .addExcludePatterns("/ex_test")
                .addIncludePatterns("/in_test");

        List<MappedInterceptor> interceptors = interceptorRegistry.getMappedInterceptors();

        Assert.assertEquals(interceptors.size(), 1);

        MappedInterceptor mappedInterceptor = interceptors.get(0);

        Assert.assertTrue(mappedInterceptor.matches("/in_test"));
        Assert.assertFalse(mappedInterceptor.matches("/ex_test"));

        mappedInterceptor.preHandle(null, null, null);
        mappedInterceptor.postHandle(null, null, null, null);
        mappedInterceptor.afterCompletion(null, null, null, null);
    }

    @Test
    public void testGetHandler() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        //测试TestHandlerInterceptor拦截器生效
        request.setRequestURI("/in_test");
        HandlerExecutionChain executionChain = requestMappingHandlerMapping.getHandler(request);

        HandlerMethod handlerMethod = executionChain.getHandler();
        Assert.assertTrue(handlerMethod.getBean() instanceof TestHandlerController);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof TestHandlerInterceptor);

      /*  //测试TestHandlerInterceptor拦截器不生效
        request.setRequestURI("/ex_test");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 0);

        //测试找不到Handler,抛出异常
        request.setRequestURI("/in_test454545");
        try {
            requestMappingHandlerMapping.getHandler(request);
        } catch (NoHandlerFoundException e) {
            System.out.println("异常URL:" + e.getRequestURL());
        }*/

        //测试Test2HandlerInterceptor拦截器对in_test2、in_test3都生效
        request.setRequestURI("/in_test2");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);

        request.setRequestURI("/in_test3");
        executionChain = requestMappingHandlerMapping.getHandler(request);
        Assert.assertEquals(executionChain.getInterceptors().size(), 1);
        Assert.assertTrue(((MappedInterceptor) executionChain.getInterceptors().get(0)).getInterceptor()
                instanceof Test2HandlerInterceptor);
    }

    @Test
    public void test2() throws NoSuchMethodException {
        TestController testController = new TestController();
        Method method = testController.getClass().getMethod("user", UserVo.class);

        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        UserVo userVo = new UserVo();
        userVo.setName("Silently9527");
        userVo.setAge(25);
        userVo.setBirthday(new Date());
        request.setContent(JSON.toJSONString(userVo).getBytes()); //模拟JSON参数

        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestBodyMethodArgumentResolver());

        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
                Object value = resolverComposite.resolveArgument(methodParameter, request, response, null, null);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test1() throws NoSuchMethodException {
        TestController testController = new TestController();
        Method method = testController.getClass().getMethod("test4",
                String.class, Integer.class, Date.class, HttpServletRequest.class);

        //构建HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(testController, method);

        //构建模拟请求的request
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "Silently9527");
        request.setParameter("age", "25");
        request.setParameter("birthday", "2020-11-12 13:00:00");

        //添加支持的解析器
        HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();
        resolverComposite.addResolver(new RequestParamMethodArgumentResolver());
        resolverComposite.addResolver(new ServletRequestMethodArgumentResolver());

        //定义转换器
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //用于查找方法参数名
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethod.getParameters().forEach(methodParameter -> {
            try {
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

                Object value = resolverComposite.resolveArgument(methodParameter, request,response, null, conversionService);
                System.out.println(methodParameter.getParameterName() + " : " + value + "   type: " + value.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void handle() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testViewName", Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.afterPropertiesSet();

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerMethod);

        System.out.println("modelAndView:");
        System.out.println(JSON.toJSONString(modelAndView));
    }

    @Test
    public void test123() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/path");

        MockHttpServletResponse response = new MockHttpServletResponse();

        Map<String, Object> model = new HashMap<>();
        model.put("name", "silently9527");
        model.put("url", "http://silently9527.cn");

        RedirectView redirectView = new RedirectView("/redirect/login");
        redirectView.render(model, request, response);

        response.getHeaderNames().forEach(headerName ->
                System.out.println(headerName + ":" + response.getHeader(headerName)));
    }
}
