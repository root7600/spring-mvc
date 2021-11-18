package com.yan;

import com.yan.annotation.RequestMapping;
import com.yan.annotation.RequestParam;
import com.yan.http.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
@Controller
@RequestMapping(path = "/test")
public class DispatcherController {

    @RequestMapping(path = "/dispatch", method = RequestMethod.GET)
    public String dispatch(@RequestParam(name = "name") String name, Model model) {
        System.out.println("DispatcherController.dispatch: name=>" + name);
        model.addAttribute("name", name);
        return "redirect:/hairui.cn";
    }
}
