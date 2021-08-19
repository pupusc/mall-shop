package com.wanmi.sbc.customer.provider.impl.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.quicklogin.WeChatQuickLoginQueryProvider;
import com.wanmi.sbc.customer.api.request.quicklogin.WeChatQuickLoginQueryReq;
import com.wanmi.sbc.customer.bean.vo.WeChatQuickLoginVo;
import com.wanmi.sbc.customer.quicklogin.model.root.WeChatQuickLogin;
import com.wanmi.sbc.customer.quicklogin.service.WeChatQuickLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 16:05
 **/
@RestController
@Validated
public class WeChatQuickLoginQueryController implements WeChatQuickLoginQueryProvider {

    @Autowired
    private WeChatQuickLoginService weChatQuickLoginService;

    @Override
    public BaseResponse<WeChatQuickLoginVo> get(@RequestBody @Valid WeChatQuickLoginQueryReq req) {
        WeChatQuickLogin weChatQuickLogin = weChatQuickLoginService.findByOpenId(req.getOpenId());
        WeChatQuickLoginVo vo = new WeChatQuickLoginVo();
        KsBeanUtil.copyPropertiesThird(weChatQuickLogin, vo);
        return BaseResponse.success(vo);
    }
}