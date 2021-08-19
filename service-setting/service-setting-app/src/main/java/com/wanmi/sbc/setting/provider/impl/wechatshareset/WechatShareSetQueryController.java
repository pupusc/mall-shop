package com.wanmi.sbc.setting.provider.impl.wechatshareset;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.redis.util.RedisStoreUtil;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.setting.api.provider.wechatshareset.WechatShareSetQueryProvider;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetByIdRequest;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoByStoreIdRequest;
import com.wanmi.sbc.setting.api.request.wechatshareset.WechatShareSetInfoRequest;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetByIdResponse;
import com.wanmi.sbc.setting.api.response.wechatshareset.WechatShareSetInfoResponse;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.wechatshareset.model.root.WechatShareSet;
import com.wanmi.sbc.setting.wechatshareset.service.WechatShareSetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>微信分享配置查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@RestController
@Validated
public class WechatShareSetQueryController implements WechatShareSetQueryProvider {
    @Autowired
    private WechatShareSetService wechatShareSetService;

    @Autowired
    private RedisService redisService;

    @Override
    public BaseResponse<WechatShareSetByIdResponse> getById(@RequestBody @Valid WechatShareSetByIdRequest wechatShareSetByIdRequest) {
        WechatShareSet wechatShareSet = wechatShareSetService.getById(wechatShareSetByIdRequest.getShareSetId());
        return BaseResponse.success(new WechatShareSetByIdResponse(wechatShareSetService.wrapperVo(wechatShareSet)));
    }

    /**
     * 查询微信分享配置API
     *
     * @return 微信分享配置详情 {@link WechatShareSetInfoResponse}
     * @author lq
     */
    @Override
    public BaseResponse<WechatShareSetInfoResponse> getInfo(@RequestBody @Valid WechatShareSetInfoRequest wechatShareSetInfoRequest) {
        WechatShareSet wechatShareSet = new WechatShareSet();
        wechatShareSet.setOperatePerson(wechatShareSetInfoRequest.getOperatePerson());
        wechatShareSet.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        WechatShareSet wechatShareSet1 = wechatShareSetService.getInfo(wechatShareSet);
        return BaseResponse.success(
                WechatShareSetInfoResponse.builder()
                        .shareAppId(wechatShareSet1.getShareAppId())
                        .shareAppSecret(wechatShareSet1.getShareAppSecret())
                        .build()
        );
    }

    @Override
    public BaseResponse<WechatShareSetInfoResponse> getInfoByStoreId(@Valid WechatShareSetInfoByStoreIdRequest wechatShareSetInfoByStoreIdRequest) {
        WechatShareSet wechatShareSet = wechatShareSetService.getInfoByStoreId(wechatShareSetInfoByStoreIdRequest.getStoreId());
        if(Objects.nonNull(wechatShareSet)){
            return BaseResponse.success(
                    WechatShareSetInfoResponse.builder()
                            .shareAppId(wechatShareSet.getShareAppId())
                            .shareAppSecret(wechatShareSet.getShareAppSecret())
                            .build()
            );
        }else {
            return BaseResponse.SUCCESSFUL();
        }
    }

    @Override
    public BaseResponse<WechatShareSetInfoResponse> getInfoCacheByStoreId(@Valid WechatShareSetInfoByStoreIdRequest request) {
        String config = redisService.getString(RedisStoreUtil.getWechatShareSet(request.getStoreId()));
        if(StringUtils.isNoneEmpty(config)){
            WechatShareSet wechatShareSet = JSONObject.parseObject(config, WechatShareSet.class);
            return BaseResponse.success(
                    WechatShareSetInfoResponse.builder()
                            .shareAppId(wechatShareSet.getShareAppId())
                            .shareAppSecret(wechatShareSet.getShareAppSecret())
                            .build()
            );
        }
        WechatShareSet wechatShareSet = wechatShareSetService.getInfoByStoreId(request.getStoreId());
        if(Objects.nonNull(wechatShareSet)){
            redisService.setString(RedisStoreUtil.getWechatShareSet(request.getStoreId()),JSONObject.toJSONString(wechatShareSet));
            return BaseResponse.success(
                    WechatShareSetInfoResponse.builder()
                            .shareAppId(wechatShareSet.getShareAppId())
                            .shareAppSecret(wechatShareSet.getShareAppSecret())
                            .build()
            );
        }else {
            return BaseResponse.SUCCESSFUL();
        }
    }

}

