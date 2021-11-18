package com.yan.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * InternalResourceView需要支持JSP、HTML的渲染
 * url: 表示JSP文件的路径
 * exposeModelAsRequestAttributes: 该方法把Model中的数据全部设置到了request中，方便在JSP中通过el表达式取值
 *
 */
public class InternalResourceView extends AbstractView{

    private String url;

    public InternalResourceView(String url) {
        this.url = url;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeModelAsRequestAttributes(model, request);

        RequestDispatcher rd = request.getRequestDispatcher(this.url);
        rd.forward(request, response);
    }

    /**
     * 把model中的数据放入到request
     *
     * @param model
     * @param request
     */
    private void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((name, value) -> {
            if (Objects.nonNull(value)) {
                request.setAttribute(name, value);
            } else {
                request.removeAttribute(name);
            }
        });
    }

    public String getUrl() {
        return url;
    }

}
