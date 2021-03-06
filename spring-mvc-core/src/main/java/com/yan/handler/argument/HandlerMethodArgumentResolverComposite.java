package com.yan.handler.argument;

import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {

        for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                return resolver.resolveArgument(parameter, request, response, container, conversionService);
            }
        }
        throw new IllegalArgumentException("Unsupported parameter type [" +
                parameter.getParameterType().getName() + "]. supportsParameter should be called first.");
        }

        public void addResolver(HandlerMethodArgumentResolver resolver) {
            this.argumentResolvers.add(resolver);
        }

        public void addResolver(List<HandlerMethodArgumentResolver> resolvers) {
            this.argumentResolvers.addAll(resolvers);
        }

        public void clear() {
            this.argumentResolvers.clear();
        }
    }

