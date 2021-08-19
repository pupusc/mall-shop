package com.wanmi.sbc.common.util.auth;

import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;


public class MyServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {

    private int executionCount;
    private long retryInterval;

    MyServiceUnavailableRetryStrategy(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }

    /**
     * retry邏輯
     */
    @Override
    public boolean retryRequest(HttpResponse response, int executionCount, org.apache.http.protocol.HttpContext context) {
        if (response.getStatusLine().getStatusCode() != 200 && executionCount < executionCount)
            return true;
        else
            return false;
    }

    /**
     * retry間隔時間
     */
    @Override
    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {
        private int executionCount;
        private long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = 1000;
        }

        public Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public MyServiceUnavailableRetryStrategy build() {
            return new MyServiceUnavailableRetryStrategy(this);
        }
    }


}
