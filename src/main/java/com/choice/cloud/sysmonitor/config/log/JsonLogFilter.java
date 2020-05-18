package com.choice.cloud.sysmonitor.config.log;


import com.choice.driver.entity.LogConst;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;

@Order(-1001)
@Component
@Slf4j
public class JsonLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 系统公共参数
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!StringUtils.isEmpty(name)) {
            String pid = name.split("@")[0];
            MDC.put(LogConst.PID, pid);
        }
        Tracer tracer = GlobalTracer.get();

        if (tracer.scopeManager().active() != null) {
            JaegerSpanContext ctx = (JaegerSpanContext) tracer.scopeManager().active().span().context();
            String traceId = Long.toHexString(ctx.getTraceId());
            String spanId = Long.toHexString(ctx.getSpanId());
            MDC.put(LogConst.TRACE_ID, traceId);
            MDC.put(LogConst.SPAN_ID, spanId);
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            MDC.put(LogConst.TRACE_ID, uuid);
            MDC.put(LogConst.SPAN_ID, uuid);
        }
        // END 业务公共参数
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
