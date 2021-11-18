package com.yan.handler.exception;

import com.yan.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * 该类用于表示被ControllerAdvice标注的类，比如TestController被标注了ControllerAdvice，那么就需要构建一个ControllerAdviceBean对象，
 * beanType为TestController;bean就是TestController的实例对象
 * hasControllerAdvice: 判断类上是否有注解ControllerAdvice，在开发handlerMapping的初始化是也有类似的操作
 * findAnnotatedBeans: 从容器中找出被ControllerAdvice标注的所有类，构建一个ControllerAdviceBean集合返回
 * ExceptionHandlerMethodResolver 当找出了所有被ControllerAdvice标注的类之后，我们还需要解析出这些类中哪些方法被注解ExceptionHandler标注过，
 * ExceptionHandlerMethodResolver就是来做这个事的。
 */
public class ControllerAdviceBean {

    private String beanName;
    private Class<?> beanType;
    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        Assert.notNull(bean, "Bean must not be null");
        this.beanType = bean.getClass();
        this.beanName = beanName;
        this.bean = bean;
    }


    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> hasControllerAdvice(entry.getValue()))
                .map(entry -> new ControllerAdviceBean(entry.getKey(), entry.getValue()))
                .collect(toList());
    }

    private static boolean hasControllerAdvice(Object bean) {
        Class<?> beanType = bean.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, ControllerAdvice.class));
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
