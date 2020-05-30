package com.option.market.auth.strategy;


import com.option.market.auth.AuthContext;

/**
 * @Description: this
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
public interface AuthStrategy {

	public void pre(AuthContext authContext);

	public void after(AuthContext authContext, Throwable throwable);

	public boolean match(AuthContext authContext);
}
