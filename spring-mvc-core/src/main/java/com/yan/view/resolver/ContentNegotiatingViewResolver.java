package com.yan.view.resolver;

import com.yan.utils.RequestContextHolder;
import com.yan.view.RedirectView;
import com.yan.view.View;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers;

    private List<View> defaultViews;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);
        View bestView = getBestView(candidateViews);
        if(Objects.nonNull(bestView)){
            return bestView;
        }
        return null;
    }

    /**
     * 根据请求找出最优视图
     * getCandidateViews: 通过视图名字使用ViewResolver解析出所有不为null的视图，如果默认视图不为空，把所有视图返回作为候选视图
     * getBestView: 从request中拿出头信息Accept，根据视图的ContentType从候选视图中匹配出最优的视图返回
     * @param candidateViews
     * @return
     */
    private View getBestView(List<View> candidateViews) {
        Optional<View> viewOptional = candidateViews.stream()
                .filter(view -> view instanceof RedirectView)
                .findAny();
        if (viewOptional.isPresent()) {
            return viewOptional.get();
        }

        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()) {
            for (View view : candidateViews) {
                if (acceptHeaders.nextElement().equals(view.getContentType())) {
                    return view;
                }
            }
        }
        return null;
    }

    /**
     * 先找出所有候选视图
     *
     * @param viewName
     * @return
     * @throws Exception
     */
    private List<View> getCandidateViews(String viewName) throws Exception {
        List<View> candidateViews = new ArrayList<>();
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(viewName);
            if (Objects.nonNull(view)) {
                candidateViews.add(view);
            }
        }
        if (!CollectionUtils.isEmpty(defaultViews)) {
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(viewResolvers, "viewResolvers can not null");
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public List<View> getDefaultViews() {
        return defaultViews;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
