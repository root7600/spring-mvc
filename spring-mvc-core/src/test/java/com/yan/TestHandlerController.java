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
public class TestHandlerController {

    @RequestMapping(path = "/ex_test", method = RequestMethod.POST)
    public void exTest() {
    }

    @RequestMapping(path = "/in_test", method = RequestMethod.POST)
    public void inTest() {
    }


    @RequestMapping(path = "/in_test2", method = RequestMethod.POST)
    public void inTest2() {
    }

    @RequestMapping(path = "/in_test3", method = RequestMethod.POST)
    public void inTest3() {
    }
}
