package com.yan.exception;

import javax.servlet.ServletException;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class MissingServletRequestParameterException extends ServletException {

    private String parameterName;

    private String parameterType;

    public MissingServletRequestParameterException(String parameterName, String parameterType) {
        super("Required " + parameterType + " parameter '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }
}
