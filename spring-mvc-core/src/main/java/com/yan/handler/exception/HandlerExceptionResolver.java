package com.yan.handler.exception;

import com.yan.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {

    ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex);
}
