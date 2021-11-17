package com.yan.handler.interceptor;

import com.yan.ModelAndView;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hairui
 * @date 2021/11/17
 * @des
 */
public class MappedInterceptor implements HandlerInterceptor{

    private List<String> includePatterns = new ArrayList<>();
    private List<String> excludePatterns = new ArrayList<>();

    private HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }


    /**
     * 添加支持的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addIncludePatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 添加排除的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addExcludePatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }


    /**
     * 根据传入的path, 判断当前的interceptor是否支持
     *
     * @param lookupPath
     * @return
     */
    public boolean matches(String lookupPath) {
        if (!CollectionUtils.isEmpty(this.excludePatterns)) {
            if (excludePatterns.contains(lookupPath)) {
                return false;
            }
        }
        if (CollectionUtils.isEmpty(this.includePatterns)) {
            return false;
        }
        if (includePatterns.contains(lookupPath)) {
            return true;
        }
        return false;
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.interceptor.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }
}
