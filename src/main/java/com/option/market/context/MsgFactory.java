package com.option.market.context;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
/**
 * @Description: this
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
@Component
public class MsgFactory {

	private ResourceBundleMessageSource messageResource;

	private String[] baseNames;

	public String[] getBaseNames() {
		return baseNames;
	}

	public void setBaseNames(String[] baseNames) {
		this.baseNames = baseNames;
	}

	private synchronized void init() {
		if (null == messageResource) {
			messageResource = new ResourceBundleMessageSource();
			messageResource.addBasenames(baseNames);
		}
	}

	/**
	 * 根据code码获取msg
	 * @param code
	 * @param args
	 * @param locale
	 * @return
	 */
	public String get(String code, Object[] args, Locale locale) {
		if (null == messageResource) {
			init();
		}
		return messageResource.getMessage(code, args, locale);
	}

}
