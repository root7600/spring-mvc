package com.yan;

import com.yan.handler.HandlerMethod;
import com.yan.handler.mapping.MappingRegistry;
import com.yan.handler.mapping.RequestMappingHandlerMapping;
import com.yan.handler.mapping.RequestMappingInfo;
import com.yan.http.RequestMethod;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
