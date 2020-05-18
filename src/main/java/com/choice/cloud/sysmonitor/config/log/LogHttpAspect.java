package com.choice.cloud.sysmonitor.config.log;

import com.choice.cloud.sysmonitor.core.bean.ResponseData;
import com.choice.driver.entity.DigestLog;
import com.choice.driver.enums.LogErrorCodeEnums;
import com.choice.driver.enums.LogOriginEnum;
import com.google.common.collect.Lists;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class LogHttpAspect {
    public static final Logger LOGGER = LoggerFactory.getLogger(LogHttpAspect.class);
    private static final String PONG = "pong";
    //***********************************************************************************************************
    //1.
//***********************************************************************************************************
    private static final List<String> SUCCESS_CODE_LIST = Lists.newArrayList("200", "10000");
    //2.
//***********************************************************************************************************
    private static final String POINTCUT = "execution(public * com.choice.qrdish.controller..*.*(..))";
//***********************************************************************************************************

    @Around(POINTCUT)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 获取当前的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUri = request.getRequestURI();
        String origin = LogOriginEnum.IN.getValue();
        String errorCode = null, status = "Y", errMsg = null;

        try {
            Object result = joinPoint.proceed();
            if (result == null) {
                return null;
            }

            if (PONG.equals(result)) {
                status = "Y";
                return result;
            }
//3.
//***********************************************************************************************************
            if (result.getClass().isAssignableFrom(ResponseData.class)) {
                ResponseData responseData = (ResponseData) result;
                if (SUCCESS_CODE_LIST.contains(responseData.getCode())) {
                    status = "Y";
                } else {
                    errorCode = responseData.getCode();
                    errMsg = responseData.getMsg();
                    status = "N";
                }
            } else if (result.getClass().isAssignableFrom(String.class)) {
                // ping/pong
            } else {
                LOGGER.debug("unknown http response type, {}", result);
            }
//***********************************************************************************************************

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            DigestLog.digestLog(requestUri, LogOrigin.HTTP_IN.getValue(), errorCode, status, elapsedTime, errMsg);

            return result;
        } catch (Throwable e) {
            errorCode = LogErrorCodeEnums.SYSTEM_ERROR.getValue();
            status = "N";
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            DigestLog.digestLog(requestUri, LogOrigin.HTTP_IN.getValue(), errorCode, status, elapsedTime, e.getMessage());
            LOGGER.error("handle http request, path = [{}] occur exception, stack trace = {}", requestUri, ExceptionUtils.getStackTrace(e));

            throw e;
        }
    }
}