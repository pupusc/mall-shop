package com.wanmi.sbc.authorize;

import com.wanmi.sbc.authorize.request.WechatAuthRequest;
import com.wanmi.sbc.authorize.util.WxDataDecryptUtils;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/authorize")
@Api(tags = "AuthorizeController", description = "小程序解析手机号码")
public class AuthorizeController {

    @ApiOperation(value = "解析手机号码")
    @RequestMapping( value = "/getPhoneNum",method = RequestMethod.POST)
    public BaseResponse<String> getPhoneNum(@RequestBody @Validated WechatAuthRequest request) {
        // 获取手机号码
        String phoneNumber = WxDataDecryptUtils.getPhoneNumber(request.getIv(), request.getSessionKey(),
                request.getEncryptedData());
        return BaseResponse.success(phoneNumber);
    }

}
