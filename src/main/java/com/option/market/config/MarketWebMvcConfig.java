package com.option.market.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.option.market.auth.resolver.AuthHeaderResolver;
import com.option.market.context.WebApiResponseFactory;
import com.option.market.converter.Void2WebapiResponseIntecptor;
import com.option.market.converter.WebapiResponseHttpConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass(WebApiResponseFactory.class)
public class MarketWebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private WebApiResponseFactory webApiResponseFactory;
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

		argumentResolvers.add(new AuthHeaderResolver());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
		converters.removeIf(converter -> converter instanceof StringHttpMessageConverter);
		WebapiResponseHttpConverter webapiResponseHttpConverter = new WebapiResponseHttpConverter();

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setCharset(StandardCharsets.UTF_8);
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.QuoteFieldNames,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteEnumUsingName,
				SerializerFeature.WriteBigDecimalAsPlain);
		fastJsonConfig.setWriteContentLength(true);
		webapiResponseHttpConverter.setFastJsonConfig(fastJsonConfig);
		webapiResponseHttpConverter.setWebApiResponseFactory(webApiResponseFactory);
		List<MediaType> lists = new ArrayList<>();
		lists.add(MediaType.APPLICATION_JSON_UTF8);
		webapiResponseHttpConverter.setSupportedMediaTypes(lists);
		converters.add(webapiResponseHttpConverter);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		Void2WebapiResponseIntecptor conventerVoidIntecptor = new Void2WebapiResponseIntecptor();
		conventerVoidIntecptor.setWebApiResponseFactory(webApiResponseFactory);
		registry.addInterceptor(conventerVoidIntecptor);
	}

}
