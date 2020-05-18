package com.choice.cloud.sysmonitor.common.util;

import com.choice.driver.entity.LogConst;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;

import java.util.UUID;

/**
 *
 */
public class TraceIdUtil {

    public static void setTraceId() {
        Tracer tracer = GlobalTracer.get();

        if (tracer.scopeManager().active() != null) {
            JaegerSpanContext ctx = (JaegerSpanContext) tracer.scopeManager().active().span().context();
            String traceId = Long.toHexString(ctx.getTraceId());
            String spanId = Long.toHexString(ctx.getSpanId());
            MDC.put(LogConst.TRACE_ID, traceId);
            MDC.put(LogConst.SPAN_ID, spanId);
        } else {
            if (MDC.get(LogConst.TRACE_ID) == null) {
                MDC.put(LogConst.TRACE_ID, UUID.randomUUID().toString().replaceAll("-", ""));
            }
        }
    }

    public static void setTraceIdByUuid() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(LogConst.TRACE_ID, uuid);
        MDC.put(LogConst.TRACE_ID, uuid);
    }

    public static void remoteTraceId() {
        //MDC.remove(LogConst.TRACE_ID);
        // MDC.remove(LogConst.SPAN_ID);
    }

    public static void addTraceIdAndTenantInfo(String tenantId, String storeId) {
        if (StringUtils.isNotEmpty(tenantId)) {
            MDC.put(LogConst.TENANT_ID, tenantId);
        }
        if (StringUtils.isNotEmpty(storeId)) {
            MDC.put("store_id", storeId);
        }
    }
}
