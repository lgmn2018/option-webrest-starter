package com.option.market.converter;

import com.alibaba.fastjson.JSON;
import com.option.market.context.WebApiResponseFactory;
import com.option.market.constant.CommonCodeConst;
import com.option.market.model.WebApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
public class Void2WebapiResponseIntecptor extends HandlerInterceptorAdapter {

    private WebApiResponseFactory webApiResponseFactory;

    public WebApiResponseFactory getWebApiResponseFactory() {
        return webApiResponseFactory;
    }



    public void setWebApiResponseFactory(WebApiResponseFactory webApiResponseFactory) {
        this.webApiResponseFactory = webApiResponseFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (!response.isCommitted()) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (handlerMethod.getMethod().getReturnType().isAssignableFrom(void.class)) {
                    Locale locale = LocaleContextHolder.getLocale();
                    WebApiResponse webApiResponse = webApiResponseFactory.get(CommonCodeConst.SERIVCE_SUCCESS, null,
                            null, locale);
                    response.setHeader("Content-type", "application/json;charset=utf-8");
                    response.setCharacterEncoding("utf-8");
                    JSON.writeJSONString(response.getWriter(), webApiResponse);
                }
            }
        }

        super.afterCompletion(request, response, handler, ex);
    }


}
