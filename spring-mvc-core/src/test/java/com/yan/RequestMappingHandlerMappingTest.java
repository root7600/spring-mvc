package com.yan;

import com.alibaba.fastjson.JSON;
import com.yan.handler.HandlerMethod;
import com.yan.handler.InvocableHandlerMethod;
import com.yan.handler.ModelAndViewContainer;
import com.yan.handler.argument.HandlerMethodArgumentResolverComposite;
import com.yan.handler.argument.ModelMethodArgumentResolver;
import com.yan.handler.argument.ServletRequestMethodArgumentResolver;
import com.yan.handler.argument.ServletResponseMethodArgumentResolver;
import com.yan.handler.mapping.MappingRegistry;
import com.yan.handler.mapping.RequestMappingHandlerMapping;
import com.yan.handler.mapping.RequestMappingInfo;
import com.yan.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import com.yan.handler.returnvalue.ViewNameMethodReturnValueHandler;
import com.yan.http.RequestMethod;
import com.yan.utils.RequestContextHolder;
import com.yan.view.InternalResourceView;
import com.yan.view.RedirectView;
import com.yan.view.View;
import com.yan.view.resolver.ContentNegotiatingViewResolver;
import com.yan.view.resolver.InternalResourceViewResolver;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class RequestMappingHandlerMappingTest extends BaseJunit4Test{

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Test
    public void test() {
        MappingRegistry mappingRegistry = requestMappingHandlerMapping.getMappingRegistry();

        String path4 = "/yanhello";
        int size = mappingRegistry.getPathHandlerMethod().size();
        Assert.assertEquals(mappingRegistry.getPathHandlerMethod().size(), 1);

      /*  HandlerMethod handlerMethod = mappingRegistry.getHandlerMethodByPath(path);
        HandlerMethod handlerMethod2 = mappingRegistry.getHandlerMethodByPath(path1);*/
        HandlerMethod handlerMethod4 = mappingRegistry.getHandlerMethodByPath(path4);

/*        Assert.assertNull(handlerMethod4);
        Assert.assertNotNull(handlerMethod);
        Assert.assertNotNull(handlerMethod2);*/


/*        RequestMappingInfo mapping = mappingRegistry.getMappingByPath(path);
        RequestMappingInfo mapping2 = mappingRegistry.getMappingByPath(handlerMethod4);*/
        RequestMappingInfo mapping2 = mappingRegistry.getMappingByPath(path4);
        Assert.assertNotNull(handlerMethod4);
        Assert.assertNotNull(mapping2);
        Assert.assertEquals(mapping2.getHttpMethod(), RequestMethod.POST);
    }

    @Test
    public void test1() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();

        Method method = controller.getClass().getMethod("testRequestAndResponse",
                HttpServletRequest.class, HttpServletResponse.class);

        //?????????handlerMethod???HandlerMethodArgumentResolverComposite
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite argumentResolver = new HandlerMethodArgumentResolverComposite();
        argumentResolver.addResolver(new ServletRequestMethodArgumentResolver());
        argumentResolver.addResolver(new ServletResponseMethodArgumentResolver());

        //???????????????????????????????????????????????????????????????????????????null
        InvocableHandlerMethod inMethod = new InvocableHandlerMethod(handlerMethod, argumentResolver, null, null);

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "Silently9527"); //????????????name

        MockHttpServletResponse response = new MockHttpServletResponse();

        //??????????????????????????????testRequestAndResponse
        inMethod.invokeAndHandle(request, response, mvContainer);

        System.out.println("????????????????????????:");
        System.out.println(response.getContentAsString());
    }

    @Test
    public void test2() throws Exception {
        TestInvocableHandlerMethodController controller = new TestInvocableHandlerMethodController();
        Method method = controller.getClass().getMethod("testViewName", Model.class);

        //?????????handlerMethod???HandlerMethodArgumentResolverComposite
        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        HandlerMethodArgumentResolverComposite argumentResolver = new HandlerMethodArgumentResolverComposite();
        argumentResolver.addResolver(new ModelMethodArgumentResolver());

        //??????testViewName????????????????????????????????????????????????????????????
        HandlerMethodReturnValueHandlerComposite returnValueHandler = new HandlerMethodReturnValueHandlerComposite();
        returnValueHandler.addReturnValueHandler(new ViewNameMethodReturnValueHandler());

        InvocableHandlerMethod inMethod = new InvocableHandlerMethod(handlerMethod, argumentResolver, returnValueHandler, null);

        ModelAndViewContainer mvContainer = new ModelAndViewContainer();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //????????????
        inMethod.invokeAndHandle(request, response, mvContainer);

        System.out.println("ModelAndViewContainer:");
        System.out.println(JSON.toJSONString(mvContainer.getModel()));
        System.out.println("viewName: " + mvContainer.getViewName());
    }

    @Test
    public void resolveViewName() throws Exception {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Accept", "text/html");
        RequestContextHolder.setRequest(request);

        View redirectView = negotiatingViewResolver.resolveViewName("redirect:/silently9527.cn");
        Assert.assertTrue(redirectView instanceof RedirectView); //?????????????????????????????????

        View forwardView = negotiatingViewResolver.resolveViewName("forward:/silently9527.cn");
        Assert.assertTrue(forwardView instanceof InternalResourceView); //

        View view = negotiatingViewResolver.resolveViewName("/silently9527.cn");
        Assert.assertTrue(view instanceof InternalResourceView); //???????????????`Accept`????????????????????????`InternalResourceView`

    }
}
