package com.wanmi.sbc.system;

import com.alibaba.fastjson.JSONObject;
import com.alipay.fc.csplatform.common.crypto.Base64Util;
import com.alipay.fc.csplatform.common.crypto.CustomerInfoCryptoUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailGetCustomerIdResponse;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.businessconfig.BusinessConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.systemconfig.SystemConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigRopResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.system.request.OnlineServiceUrlRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * 基本设置服务
 * Created by CHENLI on 2017/5/12.
 */
@Api(tags = "SystemController", description = "基本设置 API")
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;

    @Autowired
    private BusinessConfigQueryProvider businessConfigQueryProvider;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    /**
     * 查询基本设置
     */
    @ApiOperation(value = "查询基本设置")
    @Cacheable(value = "BASE_CONFIG")
    @RequestMapping(value = "/baseConfig", method = RequestMethod.GET)
    public BaseResponse<BaseConfigRopResponse> findBaseConfig() {
        return baseConfigQueryProvider.getBaseConfig();
//        CompositeResponse<BaseConfigRopResponse> response =  sdkClient.buildClientRequest()
//                .get(BaseConfigRopResponse.class, "baseConfig.query", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse());
    }

    /**
     * 查询招商页设置
     * @return
     */
    @ApiOperation(value = "查询招商页设置")
    @RequestMapping(value = "/businessConfig", method = RequestMethod.GET)
    public BaseResponse<BusinessConfigRopResponse> findConfig() {
        return businessConfigQueryProvider.getInfo();
//        CompositeResponse<BusinessConfigRopResponse> response =  sdkClient.buildClientRequest()
//                .get(BusinessConfigRopResponse.class, "businessConfig.query", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse() );
    }

    /**
     * 获取服务时间
     * @return
     */
    @ApiOperation(value = "获取服务时间")
    @RequestMapping(value = "/queryServerTime", method = RequestMethod.GET)
    public BaseResponse<Long> queryServerTime() {
        return BaseResponse.success(System.currentTimeMillis());
    }

    /**
     *  查询阿里云客服配置
     * @return
     */
    @ApiOperation(value = "查询阿里云客服配置")
    @PostMapping("/aliyun/detail")
    public BaseResponse queryAliyun(@RequestBody OnlineServiceUrlRequest onlineServiceUrlRequest){
        String url = "";
        try {
            SystemConfigResponse response =
                    systemConfigQueryProvider.list(SystemConfigQueryRequest.builder().configKey(ConfigKey.ONLINESERVICE.toValue())
                            .configType(ConfigType.ALIYUN_ONLINE_SERVICE.toValue()).delFlag(DeleteFlag.NO).status(1).build()).getContext();
            if (!response.getSystemConfigVOList().isEmpty()){
                String context = response.getSystemConfigVOList().get(0).getContext();

                Map obj = JSONObject.parseObject(context);

                //根据用户id查询用户的信息
                CustomerDetailGetCustomerIdResponse customer = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                        CustomerDetailByCustomerIdRequest.builder().customerId(onlineServiceUrlRequest.getCustomerId()).build())
                        .getContext();
                if (customer.getCustomerId() == null) {
                    return BaseResponse.error("客户不存在！");
                }
                // 还原公钥
                PublicKey publicKey = getPubKey(obj.get("key").toString());
                // 封装请求体
                JSONObject extInfo = new JSONObject();
                extInfo.put("userId", onlineServiceUrlRequest.getCustomerId());
                extInfo.put("userName",onlineServiceUrlRequest.getCustomerName());
                JSONObject cinfo = new JSONObject();
                cinfo.put("userId", onlineServiceUrlRequest.getCustomerId());
                cinfo.put("extInfo", extInfo);
                Map<String, String> map = CustomerInfoCryptoUtil.encryptByPublicKey(cinfo.toString(), publicKey);
                String params = "&key=" + map.get("key") + "&cinfo=" + map.get("text");

                String aliyunChat = obj.get("aliyunChat").toString();
                url = aliyunChat.concat(params);
            }
        } catch (Exception e){
            return BaseResponse.FAILED();
        }
        return BaseResponse.success(url);
    }

    private PublicKey getPubKey(String pubKey) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64Util.decode(pubKey));
        KeyFactory keyFactory;
        keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }
}