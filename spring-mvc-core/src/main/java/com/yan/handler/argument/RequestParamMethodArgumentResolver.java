package com.yan.handler.argument;

import com.yan.annotation.RequestParam;
import com.yan.exception.MissingServletRequestParameterException;
import com.yan.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    /**
     * supportsParameter: 判断Handler的参数是否有添加注解@RequestParam
     * resolveArgument: 从request中找指定name的参数，如果找不到用默认值赋值，如果默认值也没有，当required=true时抛出异常，否知返回null; 如果从request中找到了参数值，
     * 那么调用conversionService.convert方法转换成正确的类型
     * @param parameter
     * @param request
     * @param response
     * @param container
     * @param conversionService
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {
        RequestParam param = parameter.getParameterAnnotation(RequestParam.class);
        if (Objects.isNull(param)) {
            return null;
        }
        String value = request.getParameter(param.name());
        if (StringUtils.isEmpty(value)) {
            value = param.defaultValue();
        }
        if (!StringUtils.isEmpty(value)) {
            return conversionService.convert(value, parameter.getParameterType());
        }

        if (param.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(),
                    parameter.getParameterType().getName());
        }
        return null;
    }
}
