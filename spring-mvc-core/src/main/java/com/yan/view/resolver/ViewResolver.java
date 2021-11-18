package com.yan.view.resolver;

import com.yan.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
