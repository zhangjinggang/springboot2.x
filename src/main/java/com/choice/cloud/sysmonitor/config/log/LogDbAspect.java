package com.choice.cloud.sysmonitor.config.log;

import com.choice.driver.entity.DigestLog;
import com.choice.driver.enums.LogErrorCodeEnums;
import com.choice.driver.enums.LogOriginEnum;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogDbAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogDbAspect.class);
    //4.
//***********************************************************************************************************
    private static final String POINTCUT = "execution( * com.choice.qrdish.mapper..*.*(..))";
//***********************************************************************************************************

    @Pointcut(POINTCUT)
    private void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Signature signature = pjp.getSignature();
        String errorCode = null, status = "Y";
        String origin = LogOriginEnum.DB.getValue();
        String methodNameWithMapper = signature.toShortString();
        try {
            Object obj = pjp.proceed();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            String fullMethodNameWithMapper = signature.toLongString();
            if (fullMethodNameWithMapper.contains("com.baomidou.mybatisplus.core.mapper.BaseMapper")) {
                methodNameWithMapper = methodNameWithMapper.substring(11, methodNameWithMapper.length());
                Class[] interfaces = pjp.getTarget().getClass().getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    String realMapper = interfaces[0].getName();
                    String[] packages = realMapper.split("\\.");
                    String realMapperName = packages[packages.length - 1];
                    methodNameWithMapper = realMapperName + "." + methodNameWithMapper;
                }
            }
            DigestLog.digestLog(methodNameWithMapper, LogOrigin.DB.getValue(), errorCode, status, elapsedTime, null);
            return obj;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            errorCode = LogErrorCodeEnums.DB_ERROR.getValue();

            DigestLog.digestLog(methodNameWithMapper, LogOrigin.DB.getValue(), errorCode, status, elapsedTime, e.getMessage());
            LOGGER.error("handle db operation, method = [{}], occur exception, stack trace = {}",
                    methodNameWithMapper, ExceptionUtils.getStackTrace(e));

            throw e;
        }
    }
}