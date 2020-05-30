package com.option.market.auth.annotation;


import com.option.market.auth.enums.StrategyOperation;

import java.lang.annotation.*;

/**
 * @Description: this
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {
	String authStrategy() default "exe({})" ;
	
	StrategyOperation strategyOperation() default StrategyOperation.AND;
}

