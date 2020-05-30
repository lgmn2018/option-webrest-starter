package com.option.market.auth.advice;


import com.option.market.constant.CommonCodeConst;
import com.option.market.constant.SecurityCodeConst;
import com.option.market.exception.AppException;
import com.option.market.auth.AuthContext;
import com.option.market.auth.annotation.Strategy;
import com.option.market.auth.annotation.Strategys;
import com.option.market.auth.context.AuthExpressionContext;
import com.option.market.auth.strategy.AuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description: this
 * @Date: Create in 10:21 2020/2/22
 * @Modified by:
 */
@Component
@Aspect
@Slf4j
public class StratgyAdviceAop {
    @Autowired
    private BeanFactory beanfactory;

    private ExpressionParser parser;

    private StandardEvaluationContext simpleContext;

    @PostConstruct
    public void init() {

        if (parser == null) {
            parser = new SpelExpressionParser();
        }
        if (simpleContext == null) {
            simpleContext = new StandardEvaluationContext(new AuthExpressionContext());
        }
    }

    @Pointcut(value = "@annotation(com.option.market.auth.annotation.Strategys)")
    private void pointcut() {
    }

    @SuppressWarnings("unchecked")
    @Around(value = "pointcut()")
    public Object beforeController(ProceedingJoinPoint pjp) throws Throwable {
        Throwable throwable = null;
        Object o = null;
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();
        Strategys strategysContext = method.getAnnotation(Strategys.class);
        Strategy[] strategys = strategysContext.strategys();
        Object[] args = pjp.getArgs();
        AuthContext authContext = null;
        for (Object obj : args) {
            if (null == obj) {
                continue;
            }
            if (AuthContext.class.isAssignableFrom(obj.getClass())) {
                authContext = (AuthContext) obj;
                break;
            }
        }
        //鉴权
        for (Strategy strategy : strategys) {
            List<List<String>> beansArray = null;
            try {
                beansArray = (List<List<String>>) parser.parseExpression(strategy.authStrategy())
                        .getValue(simpleContext);
            } catch (Exception e) {
                log.error("解析表达式异常:{}", e.toString());
                throw new AppException(CommonCodeConst.SERVICE_ERROR);
            }

            boolean flag = false;
            for (List<String> beans : beansArray) {
                for (int i = 0; i < beans.size(); i++) {
                    AuthStrategy authStrategy = (AuthStrategy) beanfactory.getBean(beans.get(i));
                    if (!authStrategy.match(authContext)) {
                        break;
                    } else {
                        authStrategy.pre(authContext);
                    }
                    if (i == beans.size() - 1) {
                        flag = true;
                    }
                }
                if (flag == true) {
                    break;
                }
            }

            if (!flag) {
                throw new AppException(SecurityCodeConst.NO_PERMISSION);
            }
        }
        try {
            o = pjp.proceed();
        } catch (Throwable e) {
            throwable = e;
        }
        //鉴权后
        for (Strategy strategy : strategys) {
            List<List<String>> beansArray = null;
            try {
                beansArray = (List<List<String>>) parser.parseExpression(strategy.authStrategy())
                        .getValue(simpleContext);
            } catch (Exception e) {
                log.error("解析表达式异常:{}", e.toString());
                throw new AppException(CommonCodeConst.SERVICE_ERROR);
            }
            boolean flag = false;
            for (List<String> beans : beansArray) {
                for (int i = 0; i < beans.size(); i++) {
                    AuthStrategy authStrategy = (AuthStrategy) beanfactory.getBean(beans.get(i));
                    if (!authStrategy.match(authContext)) {
                        break;
                    } else {
                        authStrategy.after(authContext, throwable);
                    }
                    if (i == beans.size() - 1) {
                        flag = true;
                    }
                }

                if (flag) {
                    break;
                }
            }
        }
        if (throwable != null) {
            throw throwable;
        }
        return o;
    }

}
