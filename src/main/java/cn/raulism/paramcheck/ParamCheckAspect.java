package cn.raulism.paramcheck;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.TimeInterval;
import cn.raulism.common.WrapperResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@Slf4j
@Order(-1)
@Aspect
@Configuration
public class ParamCheckAspect {
    private final ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory();
    private final ExecutableValidator methodValidator = factory.getValidator().forExecutables();
    private final Validator beanValidator = factory.getValidator();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return methodValidator.validateParameters(obj, method, params);
    }

    private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
        return beanValidator.validate(bean);
    }

    @Around("execution(* cn.sx.**.*Controller.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) {
        //  获得切入目标对象
        Object target = pjp.getThis();
        // 获得切入方法参数
        Object[] args = pjp.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        // 校验以基本数据类型 为方法参数的
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);

        for (ConstraintViolation<Object> objectConstraintViolation : validResult) {
            // 此处可以抛个异常提示用户参数输入格式不正确
            return WrapperResponse.fail(objectConstraintViolation.getMessage());
        }

        // 校验以java bean对象 为方法参数的
        for (Object bean : args) {
            if (null != bean) {
                validResult = validBeanParams(bean);
                for (ConstraintViolation<Object> objectConstraintViolation : validResult) {
                    // 此处可以抛个异常提示用户参数输入格式不正确
                    return WrapperResponse.fail(objectConstraintViolation.getMessage());
                }
            }
        }
        TimeInterval interval = new TimeInterval();
        Object jsonObject;
        try {
            jsonObject = pjp.proceed(pjp.getArgs());
            long time = interval.intervalMs();
            if (time > 3000) {
                log.warn("接口:{}耗时[{}]毫秒.参数是:{}", pjp.getSignature(), time, CollUtil.newArrayList(pjp.getArgs()).toString());
            }
        } catch (Throwable throwable) {
            jsonObject = handlerException(pjp, throwable);
        }
        return jsonObject;
    }

    private Object handlerException(ProceedingJoinPoint pjp, Throwable e) {
        if (e instanceof RuntimeException) {
            log.error("RuntimeException{方法：" + pjp.getSignature() + "， 参数：" + Arrays.toString(pjp.getArgs()) + ",异常：" + e.getMessage() + "}", e);
        } else {
            log.error("异常{方法：" + pjp.getSignature() + "， 参数：" + Arrays.toString(pjp.getArgs()) + ",异常：" + e.getMessage() + "}", e);
        }
        return WrapperResponse.fail(e.getMessage());
    }
}