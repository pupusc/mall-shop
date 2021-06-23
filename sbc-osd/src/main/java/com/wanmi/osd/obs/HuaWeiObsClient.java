package com.wanmi.osd.obs;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.wanmi.osd.bean.OsdConfig;

import java.math.BigDecimal;

public class HuaWeiObsClient {

    private static final int socketTimeout= 3 * 10000;

    private static HuaWeiObsClient huaWeiObsClient = new HuaWeiObsClient();
    private HuaWeiObsClient() {}

    /**
     * 初始化obs客户端
     * @param osdConfig
     * @return
     */
    public  ObsClient init(OsdConfig osdConfig){
        ObsConfiguration config = new ObsConfiguration();
        config.setEndPoint(osdConfig.getEndPoint());
        config.setSocketTimeout(socketTimeout);
        config.setMaxErrorRetry(BigDecimal.ROUND_DOWN);
        return new ObsClient(osdConfig.getAccessKeyId(), osdConfig.getAccessKeySecret(), config);
    }

    public static HuaWeiObsClient instance() {
        return huaWeiObsClient;
    }
}
