package com.yan.handler.adapter;

import com.yan.ModelAndView;
import com.yan.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                        HandlerMethod handler) throws Exception;
}
