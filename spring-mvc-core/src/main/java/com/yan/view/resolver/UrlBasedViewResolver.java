package com.yan.view.resolver;

import com.yan.view.InternalResourceView;
import com.yan.view.RedirectView;
import com.yan.view.View;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * 当viewName以redirect:开头，那么返回RedirectView视图
 * 当viewName以forward:开头，那么返回InternalResourceView视图
 * 如果都不是，那么就执行模板方法buildView
 */
public abstract class UrlBasedViewResolver extends AbstractCachingViewResolver{

    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";

    private String prefix = "";
    private String suffix = "";


    @Override
    protected View createView(String viewName) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            return new RedirectView(redirectUrl);
        }

        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            return new InternalResourceView(forwardUrl);
        }

        return buildView(viewName);
    }

    protected abstract View buildView(String viewName);


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
