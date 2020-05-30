package com.option.market.config;

import com.option.market.context.MsgFactory;
import com.option.market.context.WebApiResponseFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiResponseConfig {


	@ConditionalOnMissingBean(WebApiResponseFactory.class)
	@Bean("webApiResponseFactory")
	public WebApiResponseFactory createWebApiResponseFactory() {

		WebApiResponseFactory webApiResponseFactory = new WebApiResponseFactory();
		MsgFactory msgFactory = new MsgFactory();
		msgFactory.setBaseNames(new String[] { "SysCode" });
		webApiResponseFactory.setMsgFactory(msgFactory);
		return webApiResponseFactory;
	}

}
