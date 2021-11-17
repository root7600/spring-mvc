package com.yan;

import com.yan.annotation.RequestMapping;
import com.yan.http.RequestMethod;
import org.springframework.stereotype.Controller;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
@Controller
@RequestMapping(path = "/yan")
public class ControllerTest {

    @RequestMapping(path = "hello",method = RequestMethod.GET)
    public void  sayHello(){

        System.out.println("HELLO~ SPRING-MVC ");
    }

}
