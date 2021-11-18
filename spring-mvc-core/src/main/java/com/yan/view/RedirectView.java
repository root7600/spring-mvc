package com.yan.view;

import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/18
 * @des
 * 定义url，表示重定向的地址，实际也就是控制器中返回的视图名截取redirect:之后的字符串
 * createTargetUrl: 根据url拼接出重定向的地址，如果有设置contentPath，需要把contentPath拼接到链接的前面；
 * 如果Model中有属性值，需要把model中的属性值拼接到链接后面
 */
public class RedirectView extends AbstractView{

    private String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String targetUrl = createTargetUrl(model, request);
        response.sendRedirect(targetUrl);
    }

    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(this.url, "url can not null");
        StringBuilder stringBuilder = new StringBuilder();
        model.forEach((key,value)->{
            stringBuilder.append(key).append("=").append(value).append("&");
        });
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        StringBuilder targetUrl = new StringBuilder();
        if (this.url.startsWith("/")) {
            // Do not apply context path to relative URLs.
            targetUrl.append(getContextPath(request));
        }

        targetUrl.append(url);

        if (stringBuilder.length() > 0) {
            targetUrl.append("?").append(stringBuilder.toString());
        }
        return targetUrl.toString();

    }

    private String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        while (contextPath.startsWith("//")) {
            contextPath = contextPath.substring(1);
        }
        return contextPath;
    }
}
