package com.wanmi.osd.cos;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.wanmi.osd.bean.OsdConfig;


public class TxCosClient {
    private static final int connectionTimeout = 3 * 1000;

    private static final int socketTimeout = 3 * 1000;

    private static TxCosClient txCosClient = new TxCosClient();

    private TxCosClient() {}

    /**
     * 初始化cos客户端
     * @param osdConfig
     * @return
     */
    public COSClient init( OsdConfig osdConfig){
        COSCredentials cred = new BasicCOSCredentials(osdConfig.getAccessKeyId(), osdConfig.getAccessKeySecret());
        Region region = new Region(osdConfig.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setConnectionTimeout(connectionTimeout);
        clientConfig.setSocketTimeout(socketTimeout);
        return new COSClient(cred,clientConfig);
    }

    public static TxCosClient instance() {
        return txCosClient;
    }
}
