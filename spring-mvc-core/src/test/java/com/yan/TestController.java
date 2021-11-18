package com.yan;

import com.yan.annotation.RequestBody;
import com.yan.annotation.RequestMapping;
import com.yan.annotation.RequestParam;
import com.yan.http.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class TestController {


    @RequestMapping(path = "/test4", method = RequestMethod.POST)
    public void test4(@RequestParam(name = "name") String name,
                      @RequestParam(name = "age") Integer age,
                      @RequestParam(name = "birthday") Date birthday,
                      HttpServletRequest request) {
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public void user(@RequestBody UserVo userVo) {
    }

}
