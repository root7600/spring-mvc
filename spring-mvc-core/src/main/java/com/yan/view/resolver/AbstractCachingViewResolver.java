package com.yan.view.resolver;

import com.yan.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * 因为启动一直一般会运行很长时间，很多用户都会请求同一个视图名称，为了避免每次都需要把viewName解析成View，
 * 所以我们需要做一层缓存， 当有一次成功解析了viewName之后我们把返回的View缓存起来，下次直接先从缓存中取
 */
public abstract class AbstractCachingViewResolver implements ViewResolver{

    private final Object lock = new Object();

    private static final View UNRESOLVED_VIEW = (model, request, response) -> {};

    private Map<String, View> cachedViews = new HashMap<>();

    /**
     * 定义一个默认的空视图UNRESOLVED_VIEW，当通过viewName解析不到视图返回null时，把默认的视图放入到缓存中
     * 由于可能存在同一时刻多个用户请求到同一个视图，所以需要使用synchronized加锁
     * 如果缓存中获取到的视图是UNRESOLVED_VIEW，那么就返回null
     * @param viewName
     * @return
     * @throws Exception
     */
    @Override
    public View resolveViewName(String viewName) throws Exception {
        View view = cachedViews.get(viewName);
        if (Objects.nonNull(view)) {
            return (view != UNRESOLVED_VIEW ? view : null);
        }

        synchronized (lock) {
            view = cachedViews.get(viewName);
            if (Objects.nonNull(view)) {
                return (view != UNRESOLVED_VIEW ? view : null);
            }

            view = createView(viewName);
            if (Objects.isNull(view)) {
                view = UNRESOLVED_VIEW;
            }
            cachedViews.put(viewName, view);
        }
        return (view != UNRESOLVED_VIEW ? view : null);
    }

    protected abstract View createView(String viewName);
}
