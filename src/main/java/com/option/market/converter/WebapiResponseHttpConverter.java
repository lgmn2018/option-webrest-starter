package com.option.market.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.option.market.context.WebApiResponseFactory;
import com.option.market.constant.CommonCodeConst;
import com.option.market.model.WebApiResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

/**
 * @Description: this
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
public class WebapiResponseHttpConverter extends FastJsonHttpMessageConverter {

	private WebApiResponseFactory webApiResponseFactory;

	public WebApiResponseFactory getWebApiResponseFactory() {
		return webApiResponseFactory;
	}

	public void setWebApiResponseFactory(WebApiResponseFactory webApiResponseFactory) {
		this.webApiResponseFactory = webApiResponseFactory;
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		HttpHeaders headers = outputMessage.getHeaders();
		ByteArrayOutputStream outnew = new ByteArrayOutputStream();

		boolean writeAsToString = false;
		if (obj != null) {
			String className = obj.getClass().getName();
			if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(className)) {
				writeAsToString = true;
			}
		}
		if (writeAsToString) {
			String text = obj.toString();
			OutputStream out = outputMessage.getBody();
			out.write(text.getBytes());
			if (getFastJsonConfig().isWriteContentLength()) {
				headers.setContentLength(text.length());
			}
		} else {
			if (!(obj instanceof WebApiResponse)) {
				Locale locale = LocaleContextHolder.getLocale();
				obj = webApiResponseFactory.get(CommonCodeConst.SERIVCE_SUCCESS, null, obj, locale);
			}
			int len = JSON.writeJSONString(outnew, //
					getFastJsonConfig().getCharset(), //
					obj, //
					getFastJsonConfig().getSerializeConfig(), //
					getFastJsonConfig().getSerializeFilters(), //
					getFastJsonConfig().getDateFormat(), //
					JSON.DEFAULT_GENERATE_FEATURE, //
					getFastJsonConfig().getSerializerFeatures());
			if (getFastJsonConfig().isWriteContentLength()) {
				headers.setContentLength(len);
			}

			OutputStream out = outputMessage.getBody();
			outnew.writeTo(out);
		}

		outnew.close();

	}

}
