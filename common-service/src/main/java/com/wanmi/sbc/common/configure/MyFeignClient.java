package com.wanmi.sbc.common.configure;

import com.alibaba.fastjson.JSON;
import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/25 2:01 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public class MyFeignClient extends Client.Default {
    /**
     * Null parameters imply platform defaults.
     *
     * @param sslContextFactory
     * @param hostnameVerifier
     */
    public String sourceProjectName;
    public MyFeignClient(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, String sourceProjectName) {
        super(sslContextFactory, hostnameVerifier);
        this.sourceProjectName = sourceProjectName;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        try {
            log.info("{} invoke url: {} method:{} request:{} options:{} ", sourceProjectName,
                    request.url(), request.httpMethod(), JSON.toJSONString(request), JSON.toJSONString(options));
            return super.execute(request, options);
        } catch (Exception e) {
            log.error("请求{} 异常", request.url(), e);
            throw e;
        }
    }
}
