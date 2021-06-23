package com.wanmi.sbc.erp.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.erp.response.ERPBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @program: sbc-background
 * @description: 管易云ERP接口调用工具类
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 19:40
 **/
@Slf4j
@Component
public class GuanyierpUtil {

    @Value("${guanyierp_secret}")
    private String secret;

    protected CloseableHttpClient client = HttpClientBuilder.create().build();

    // The user agent
    protected final String USER_AGENT = "Mozilla/5.0";

    /**
     * 生产签名数据
     * @param objectJsonStr
     * @return
     */
    public String buildSign(String objectJsonStr){
        StringBuilder  enValue = new StringBuilder();
        enValue.append(secret);
        enValue.append(objectJsonStr);
        enValue.append(secret);
        String sign = MD5Util.md5Hex(enValue.toString(),"utf-8");
        return sign;
    }

    /**
     * 调用ERP接口工具方法
     * @param url
     * @param data
     * @return
     */
    public String execute(String url,String data){
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(data, UTF_8);
        post.setEntity(se);
        // Send the post request and get the response
        try (CloseableHttpResponse response = client.execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                log.debug("erpClient.execute_请求成功");
                String strResult = EntityUtils.toString(response.getEntity(), UTF_8);
                log.info("erpClient.execute_成功返回json::{}", strResult);
                return strResult;
            } else {
                log.error("erpClient.execute_请求失败异常,code::{}", status);
                log.error("erpClient.execute_请求失败异常,entry::{}", EntityUtils.toString(response.getEntity(), UTF_8));
                throw new SbcRuntimeException("K-300502");
            }
        } catch (IOException e) {
            log.error("erpClient.execute_请求IO异常", e);
            throw new SbcRuntimeException("K-300503");
        }
    }
}
