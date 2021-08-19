package com.wanmi.sbc.customer.quicklogin.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.quicklogin.model.root.WeChatQuickLogin;
import com.wanmi.sbc.customer.quicklogin.repository.WeChatQuickLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 15:56
 **/
@Service
public class WeChatQuickLoginService {

    @Autowired
    private WeChatQuickLoginRepository weChatQuickLoginRepository;

    public WeChatQuickLogin save(WeChatQuickLogin req){
        WeChatQuickLogin weChatQuickLogin = weChatQuickLoginRepository.findByOpenIdAndDelFlag(req.getOpenId(),
                DeleteFlag.NO).orElseGet(WeChatQuickLogin::new);;
        KsBeanUtil.copyProperties(req, weChatQuickLogin);
        return weChatQuickLoginRepository.save(weChatQuickLogin);
    }

    public WeChatQuickLogin findByOpenId(String openId){
        return weChatQuickLoginRepository.findByOpenIdAndDelFlag(openId, DeleteFlag.NO).orElseGet(WeChatQuickLogin::new);
    }
}