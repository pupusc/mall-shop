package com.wanmi.sbc.message.configuration.xxljob;

import brave.ScopedSpan;
import brave.Tracing;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cloud.sleuth.DefaultSpanNamer;
import org.springframework.cloud.sleuth.SpanNamer;

/**
 * \* Author: zgl
 * \* Date: 2020-2-6
 * \* Time: 10:34
 * \* Description:
 * \
 */
@Slf4j
@RequiredArgsConstructor
public class XxlJobTraceWrapper extends IJobHandler {
    private final BeanFactory beanFactory;

    private final IJobHandler delegate;

    private Tracing tracing;

    private SpanNamer spanNamer;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        if (this.tracing == null) {
            try {
                this.tracing = this.beanFactory.getBean(Tracing.class);
            } catch (NoSuchBeanDefinitionException e) {
                return this.delegate.execute(param);
            }
        }

        return doExecute(param);
    }

    private ReturnT<String> doExecute(String param) throws Exception {
        ScopedSpan span = this.tracing.tracer().startScopedSpanWithParent(spanNamer().name(delegate, "XXJOB"),
                this.tracing.currentTraceContext().get());
        try {
            return this.delegate.execute(param);
        } catch (Exception | Error e) {
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }

    }

    // due to some race conditions trace keys might not be ready yet
    private SpanNamer spanNamer() {
        if (this.spanNamer == null) {
            try {
                this.spanNamer = this.beanFactory.getBean(SpanNamer.class);
            } catch (NoSuchBeanDefinitionException e) {
                log.warn("SpanNamer bean not found - will provide a manually created instance");
                return new DefaultSpanNamer();
            }
        }
        return this.spanNamer;
    }
}