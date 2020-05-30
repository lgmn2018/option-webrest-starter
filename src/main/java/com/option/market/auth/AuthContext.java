package com.option.market.auth;

import com.option.market.constant.CommonCodeConst;
import com.option.market.exception.AppException;
import com.option.market.model.LoginSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class AuthContext {
    private static final String FILED_SEPARATOR = ",";
    private static final String VALUE_SEPARATOR = "=";
    private Properties properties;
    private static final String PAY_PASSWORD = "pay-password";
    private static final String LOGIN_PASSWORD = "login-password";

    private static final String TOKEN = "token";
    private HttpServletRequest request;

    private static final String LOGIN = "loginsession";

    NativeWebRequest webRequest;

    public static AuthContext build(String headerStr, NativeWebRequest webRequest) {
        AuthContext authContext = new AuthContext();
        String[] fields = StringUtils.tokenizeToStringArray(headerStr, FILED_SEPARATOR, true, true);

        try {
            authContext.properties = Optional.ofNullable(splitArrayElementsIntoProperties(fields, VALUE_SEPARATOR, null)).orElse(new Properties());
        } catch (UnsupportedEncodingException e) {
            log.error("解析请求头参数失败：e:{}", e.toString());
            throw new AppException(CommonCodeConst.FIELD_ERROR);
        }

        authContext.webRequest = webRequest;
        return authContext;
    }

    public HttpServletRequest getRequest() {
        return webRequest.getNativeRequest(HttpServletRequest.class);
    }

    public String getUri() {
        return webRequest.getNativeRequest(HttpServletRequest.class).getRequestURI();
    }

    public String getField(String field) {
        return properties.getProperty(field);
    }


    public String getpayPassword() {
        return properties.getProperty(PAY_PASSWORD);
    }

    public String getLoginPassword() {
        return properties.getProperty(LOGIN_PASSWORD);
    }

    public LoginSession getLoginSession() {
        Object o = webRequest.getAttribute(LOGIN, RequestAttributes.SCOPE_REQUEST);
        if (null == o)
            return null;
        return (LoginSession) o;
    }

    public void setLoginSession(LoginSession loginSession) {
        webRequest.setAttribute(LOGIN, loginSession, RequestAttributes.SCOPE_REQUEST);
    }

    public String getToke() {
        return properties.getProperty(TOKEN);
    }

    @Override
    public String toString() {
        return "AuthContext [properties=" + properties + "]";
    }

    /**
     * 获取请求头参数token等进行切分处理
     *
     * @param array
     * @param delimiter     分隔符
     * @param charsToDelete
     * @return
     * @throws UnsupportedEncodingException
     */
    private static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete)
            throws UnsupportedEncodingException {

        if (ObjectUtils.isEmpty(array)) {
            return null;
        }
        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = StringUtils.deleteAny(element, charsToDelete);
            }
            String[] splittedElement = StringUtils.split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), URLDecoder.decode(splittedElement[1].trim(), "UTF-8"));
        }
        return result;
    }

}
