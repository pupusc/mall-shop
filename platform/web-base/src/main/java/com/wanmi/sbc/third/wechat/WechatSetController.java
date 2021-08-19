package com.wanmi.sbc.third.wechat;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.third.wechat.WechatSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "WechatSetController", description = "第三方登录-微信 API")
@RestController
@RequestMapping("/third/wechat")
public class WechatSetController {

    @Autowired
    private WechatSetService wechatSetService;

    /**
     * 根据类型获取
     *
     * @return
     */
    @ApiOperation(value = "获取微信设置开关", notes = "terminalType: PC,H5,APP,MINI")
    @ApiImplicitParam(paramType = "path", name = "terminalType", value = "类型终端", required = true)
    @RequestMapping(value = "/status/{terminalType}", method = RequestMethod.GET)
    public BaseResponse<DefaultFlag> status(@PathVariable String terminalType) {
        return BaseResponse.success(wechatSetService.getStatus(TerminalType.valueOf(terminalType)));
    }
}
