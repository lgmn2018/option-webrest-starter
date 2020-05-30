package com.option.market.auth.resolver;

import com.option.market.auth.annotation.AuthForHeader;
import com.option.market.auth.AuthContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Description: this 以参数方式判断该接口是否处理请求头参数
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
public class AuthHeaderResolver implements HandlerMethodArgumentResolver {
	//请求头鉴权处理参数
	private static final String AUTH_HEADER = "authorization";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		return parameter.hasParameterAnnotation(AuthForHeader.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		String header = webRequest.getHeader(AUTH_HEADER);
		if (!parameter.getParameterType().isAssignableFrom(AuthContext.class)) {
			return null;
		} else {
			return AuthContext.build(header, webRequest);
		}

	}

}
