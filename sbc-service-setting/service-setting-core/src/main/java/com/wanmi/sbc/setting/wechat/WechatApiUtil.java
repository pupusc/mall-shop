package com.wanmi.sbc.setting.wechat;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.util.RedisStoreUtil;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.request.MiniProgramSetContextRequest;
import com.wanmi.sbc.setting.api.request.MiniProgramSetRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.config.Config;
import com.wanmi.sbc.setting.config.ConfigRepository;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root.StoreWechatMiniProgramConfig;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.service.StoreWechatMiniProgramConfigService;
import com.wanmi.sbc.setting.wechat.constant.WechatApiConstant;
import com.wanmi.sbc.setting.yunservice.YunService;
import jodd.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 调用微信api接口方法
 */
@Component
@Slf4j
public class WechatApiUtil {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private YunService yunService;

    @Value("${wechat.access-token.cache}")
    private boolean accessTokenCache;

    @Autowired
    private StoreWechatMiniProgramConfigService storeWechatMiniProgramConfigService;

    /**
     * 获取access_token参数接口
     *
     * @return
     */
    public String getAccessToken(String type) {
        String accessToken = null;
        String redisKey = "get_access_token_" + type;
        // 1.是否先从缓存中获取
        if (accessTokenCache) {
            // 1.1.从redis缓存中获取
            accessToken = redisService.getString(redisKey);
        }
        if (StringUtils.isEmpty(accessToken)) {
            try {
                // 1.2.通过接口获取（并设置缓存失效时间为7000秒，access_token失效时间为7200秒）
                Config config = configRepository.findByConfigTypeAndDelFlag(WechatApiConstant.SMALL_PROGRAM_SETTING_CUSTOMER, DeleteFlag.NO);
                //如果禁用,直接return null
                if (config.getStatus().equals(0)) {
                    return null;
                }
                JSONObject configJson = JSONObject.parseObject(config.getContext());
                String appId = configJson.getString("appId");
                String appSecret = configJson.getString("appSecret");
                String url = String.format(WechatApiConstant.ACCESS_TOKEN_URL, WechatApiConstant.GRANT_TYPE, appId, appSecret);
                String accessTokenResStr = doGet(url);
                log.info("获取access_token接口url:{}，返回结果：{}",url,accessTokenResStr);
                if (StringUtils.isNotEmpty(accessTokenResStr)) {
                    JSONObject tokenResJson = JSONObject.parseObject(accessTokenResStr);
                    accessToken = tokenResJson.getString("access_token");
                    if (StringUtils.isNotEmpty(accessToken)) {
                        redisService.setString(redisKey, accessToken);
                        // accessToken失效时间调小，五分钟
                        redisService.expireBySeconds(redisKey, 300L);
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        if (StringUtils.isEmpty(accessToken)) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "获取accessToken失败");
        }
        log.info("小程序type:{}===AccessToken:{}", type, accessToken);
        return accessToken;
    }

    /**
     * 门店id获取accessToken
     * @param storeId
     * @return
     */
    public String getAccessTokenByStoreId(Long storeId) {
        String wechatAccessTokenKey = RedisStoreUtil.getWechatAccessToken(storeId);
        // 从缓存中获取开关
        if(accessTokenCache){
            String token = redisService.getString(wechatAccessTokenKey);
            if(StringUtils.isNotEmpty(token)){
                return token;
            }
        }
        StoreWechatMiniProgramConfig config = storeWechatMiniProgramConfigService.getByStoreId(storeId);
        if(config == null){
            return null;
        }
        String accessToken=null;
        try {
            String appId = config.getAppId();
            String appSecret = config.getAppSecret();
            String url = String.format(WechatApiConstant.ACCESS_TOKEN_URL, WechatApiConstant.GRANT_TYPE, appId, appSecret);
            String accessTokenResStr = doGet(url);
            if (StringUtils.isNotEmpty(accessTokenResStr)) {
                JSONObject tokenResJson = JSONObject.parseObject(accessTokenResStr);
                accessToken = tokenResJson.getString("access_token");
                if (StringUtils.isNotEmpty(accessToken)) {
                    redisService.setString(wechatAccessTokenKey, accessToken);
                    // access_token失效时间为7200s,所以设置生命周期为6000s
                    redisService.expireBySeconds(wechatAccessTokenKey, 6000L);
                }
            }
        } catch (Exception e) {
            log.error("小程序accessToken识别失败！"+"，storeId："+storeId+"exception："+ ExceptionUtil.message(e));
        }
        return accessToken;
    }

    /**
     * 通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param request 入参
     * @return
     */
    public String getWxaCodeUnlimit(MiniProgramQrCodeRequest request, String accessTokenType) {
        String imagesUrl = this.getQrImageCache(request);
        if(StringUtils.isNotBlank(imagesUrl)){
            return imagesUrl;
        }
        byte[] qrCodeJson = this.getWxaCodeBytesUnlimit(request, accessTokenType);
        //上传小程序码到oss上,并返回地址给调用方
        String image= yunService.uploadFile(YunUploadResourceRequest.builder()
                .resourceName("小程序码.jpg")
                .content(qrCodeJson)
                .resourceType(ResourceType.IMAGE)
                .build()).getResourceUrl();
        this.updateQrImageCache(request, image);
        return image;
    }

    /**
     * 通过该接口生成的小程序码，永久有效，数量暂无限制
     *
     * @param request 入参
     * @return
     */
    public byte[] getWxaCodeBytesUnlimit(MiniProgramQrCodeRequest request, String accessTokenType) {
        String accessToken = getAccessToken(accessTokenType);
        String params = JSONObject.toJSONString(request);

        if(accessToken == null){
            return null;
        }
        // 1.获取微信小程序码字节数组
        String url = String.format(WechatApiConstant.GET_WX_A_CODE_UNLIMIT, accessToken);
        byte[] qrCodeJson = doPostSpecial(url, params);
        // 2.若获取小程序码错误,将返回json格式的对象(而不是文件流)
        String resStr = new String(qrCodeJson);
        if (resStr.contains("errcode")) {
            //报token问题
            if (JSONObject.parseObject(resStr).getString("errcode").equals("40001")) {
                //删除redis缓存重新生成
                redisService.delete("get_access_token_PUBLIC");
                qrCodeJson = doPostSpecial(String.format(WechatApiConstant.GET_WX_A_CODE_UNLIMIT, getAccessToken(accessTokenType)),
                        params);

            } else {
                log.error("生成小程序码失败！"+"，参数："+JSONObject.toJSONString(request)+",exception："+resStr);
                throw new RuntimeException(resStr);
            }
        }
        return qrCodeJson;
    }

    /**
     * get请求，参数拼接在地址上
     *
     * @param url 请求地址加参数
     * @return 字符串类型的返回值
     */
    public String doGet(String url) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * post请求
     *
     * @param url     请求地址
     * @param jsonStr json格式的参数
     * @return JSONObject返回对象
     */
    public JSONObject doPost(String url, String jsonStr) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
            HttpResponse res = httpClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());
                response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * post请求
     * 为了小程序码,单独写的方法,因为小程序码接口返回的是文件流
     *
     * @param url     请求地址
     * @param jsonStr json格式的参数
     * @return 字节数组
     */
    private byte[] doPostSpecial(String url, String jsonStr) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        byte[] out = null;
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(jsonStr, Charset.forName("UTF-8")));
            HttpResponse res = httpClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                if (entity.isStreaming()) {
                    InputStream input = entity.getContent();
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = input.read(buff)) != -1) {
                        byteOut.write(buff, 0, len);
                    }
                    out = byteOut.toByteArray();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }


    /**
     * 获取小程序配置
     *
     * @return
     */
    public Config getMiniProgramSet() {
        Config config = configRepository.findByConfigTypeAndDelFlag(WechatApiConstant.SMALL_PROGRAM_SETTING_CUSTOMER, DeleteFlag.NO);
        if (config != null) {
            return config;
        }
        return null;
    }


    /**
     * 修改小程序配置
     *
     * @param miniProgramSetRequest
     * @return
     */
    @Transactional
    public BaseResponse updateMiniProgramSet(MiniProgramSetRequest miniProgramSetRequest) {
        //用一个新对象接收appId和appSecret的值
        MiniProgramSetContextRequest request = new MiniProgramSetContextRequest();
        request.setAppId(miniProgramSetRequest.getAppId());
        request.setAppSecret(miniProgramSetRequest.getAppSecret());
        //清空accessToken的缓存
        String redisKey = "get_access_token_PUBLIC";
        redisService.delete(redisKey);
        //清空image的缓存
        redisService.delete(RedisKeyConstant.QR_CODE_CACHE);
        configRepository.updateStatusByTypeAndConfigKey(ConfigType.SMALL_PROGRAM_SETTING_CUSTOMER.toValue(), ConfigKey.SMALL_PROGRAM_SETTING
                .toString(), miniProgramSetRequest.getStatus(), JSONObject.toJSONString(request));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取二维码图片（缓存）
     * @param request
     * @return
     */
    private String getQrImageCache(MiniProgramQrCodeRequest request){
        String key = Objects.toString(request.getPage()).concat(Objects.toString(request.getScene()));
        return redisService.hget(RedisKeyConstant.QR_CODE_CACHE, key);
    }

    /**
     * 更新二维码图片（缓存）
     * @param request
     * @param imageUrl
     */
    private void updateQrImageCache(MiniProgramQrCodeRequest request, String imageUrl){
        String key = Objects.toString(request.getPage()).concat(Objects.toString(request.getScene()));
        boolean isNew = true;
        if(redisService.hasKey(RedisKeyConstant.QR_CODE_CACHE)){
            isNew = false;
        }
        redisService.hset(RedisKeyConstant.QR_CODE_CACHE, key, imageUrl);
        if(isNew) {
            //新增的设定5500秒失效，跟着token失效时间一样
            redisService.expireBySeconds(RedisKeyConstant.QR_CODE_CACHE, 6000L);
        }
    }
}
