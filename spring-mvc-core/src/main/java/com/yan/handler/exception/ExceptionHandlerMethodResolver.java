package com.yan.handler.exception;

import com.yan.annotation.ExceptionHandler;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * 首先定义了EXCEPTION_HANDLER_METHODS静态变量，判断方法是否有注解ExceptionHandler
 * detectExceptionMappings: 解析出方法上ExceptionHandler配置的所有异常
 * 构造方法中传入了Bean的类型，使用MethodIntrospector.selectMethods过滤出所有被ExceptionHandler标注的类（在HanderMapping的初始化也使用过同样的方法），
 * 保存异常类型和方法的对应关系
 * resolveMethod: 通过异常类型找出对应的方法
 */
public class ExceptionHandlerMethodResolver {

    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = method -> AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);

    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentReferenceHashMap<>(16);

    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                this.mappedMethods.put(exceptionType, method);
            }
        }
    }

    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
        ExceptionHandler ann = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(ann != null, "No ExceptionHandler annotation");
        return Arrays.asList(ann.value());
    }

    public Map<Class<? extends Throwable>, Method> getMappedMethods() {
        return mappedMethods;
    }

    public boolean hasExceptionMappings() {
        return !this.mappedMethods.isEmpty();
    }

    public Method resolveMethod(Exception exception) {
        Method method = resolveMethodByExceptionType(exception.getClass());
        if (method == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                method = resolveMethodByExceptionType(cause.getClass());
            }
        }
        return method;
    }

    private Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionClass) {
        return mappedMethods.get(exceptionClass);
    }
}
