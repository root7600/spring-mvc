package com.yan.view.resolver;

import com.yan.view.InternalResourceView;
import com.yan.view.View;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public class InternalResourceViewResolver extends UrlBasedViewResolver{

    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
