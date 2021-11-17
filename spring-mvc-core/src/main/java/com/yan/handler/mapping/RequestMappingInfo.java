package com.yan.handler.mapping;

import com.yan.annotation.RequestMapping;
import com.yan.http.RequestMethod;

/**
 * @author hairui
 * @date 2021/11/17
 * @des 将注解的属性转为对象
 */
public class RequestMappingInfo {

    private String path;
    private RequestMethod httpMethod;

    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.httpMethod = requestMapping.method();
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getHttpMethod() {
        return httpMethod;
    }
}
