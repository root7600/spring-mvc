package com.yan.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 */
public abstract class AbstractView implements View{

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.prepareResponse(request, response);
        this.renderMergedOutputModel(model, request, response);
    }

    /**
     * 在实施渲染之前需要做的一些工作放入到这个方法中，比如：设置响应的头信息
     * @param request
     * @param response
     */
    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 执行渲染的逻辑都将放入到这个方法中
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
