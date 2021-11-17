package com.yan.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class NoHandlerFoundException extends ServletException {

    private String httpMethod;

    private String requestURL;

    public NoHandlerFoundException(HttpServletRequest request) {
        this.httpMethod = request.getMethod();
        this.requestURL = request.getRequestURL().toString();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
