package com.wanmi.osd.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.wanmi.osd.bean.OsdConfig;

public class AliOssClient {

    private static final int connectionTimeout = 3 * 1000;

    private static final int socketTimeout = 3 * 1000;

    private static AliOssClient aliOssClient = new AliOssClient();

    private AliOssClient() {}

    /**
     * 初始化oss客户端
     * @param osdConfig
     * @return
     */
    public OSSClient init(OsdConfig osdConfig){
        ClientConfiguration config = new ClientConfiguration();
        // 建立连接的超时时间（单位：毫秒）
        config.setConnectionTimeout(connectionTimeout);
        // 通过打开的连接传输数据的超时时间（单位：毫秒）
        config.setSocketTimeout(socketTimeout);
        return new OSSClient(osdConfig.getEndPoint(), osdConfig.getAccessKeyId(), osdConfig.getAccessKeySecret(), config);
    }

    public static AliOssClient instance() {
        return aliOssClient;
    }
}
