package com.option.market.auth.strategy;

import com.option.market.auth.AuthContext;

/**
 * @Description: this 鉴权处理抽象类
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
public abstract class AbstractAuthStrategy implements AuthStrategy {
    @Override
    public void pre(AuthContext authContext) {

    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {

    }

    @Override
    public boolean match(AuthContext authContext) {
        return true;
    }
}
