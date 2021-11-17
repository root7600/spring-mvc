package com.yan.handler.mapping;

import com.yan.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class MappingRegistry {

    private Map<String, RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    private Map<String, HandlerMethod> pathHandlerMethod = new ConcurrentHashMap<>();


    /**
     * 注册url和Mapping/HandlerMethod的对应关系
     *
     * @param mapping
     * @param handler
     * @param method
     */
    public void register(RequestMappingInfo mapping, Object handler, Method method) {
        pathMappingInfo.put(mapping.getPath(), mapping);

        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHandlerMethod.put(mapping.getPath(), handlerMethod);
    }

    public Map<String, RequestMappingInfo> getPathMappingInfo() {
        return pathMappingInfo;
    }

    public Map<String, HandlerMethod> getPathHandlerMethod() {
        return pathHandlerMethod;
    }

    public RequestMappingInfo getMappingByPath(String path) {
        return this.pathMappingInfo.get(path);
    }

    public HandlerMethod getHandlerMethodByPath(String path) {
        return this.pathHandlerMethod.get(path);
    }

}
