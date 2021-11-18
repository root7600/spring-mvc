package com.yan;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class DispatcherServletTest extends BaseJunit4Test{

    @Autowired
    private DispatcherServlet dispatcherServlet;

    @Test
    public void service() throws ServletException, IOException {
        dispatcherServlet.init(); //初始化

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "hairui"); //设置请求的参数name
        request.setRequestURI("/test/dispatch"); //设置请求的URI

        MockHttpServletResponse response = new MockHttpServletResponse();

        dispatcherServlet.service(request, response);

        //打印出头信息，判断是否正常的rediect，并且带上了name参数
        response.getHeaderNames().forEach(headerName ->
                System.out.println(headerName + ":" + response.getHeader(headerName)));
    }
}
