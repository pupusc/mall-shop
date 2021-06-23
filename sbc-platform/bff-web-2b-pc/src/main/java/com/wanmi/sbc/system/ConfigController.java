package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.response.GoodsDisplayConfigGetResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 获取后台配置项
 * Created by xmn on 2018/10/10.
 */
@Api(tags = "ConfigController", description = "获取后台配置项Api")
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private AuditQueryProvider auditQueryProvider;

    @Resource
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 前台商品列表默认展示维度（大小图、spu|sku）
     *
     * @return
     */
    @ApiOperation(value = "前台商品列表默认展示维度(大小图、spu|sku)")
    @RequestMapping(value = "/goodsDisplayDefault", method = RequestMethod.GET)
    public BaseResponse<GoodsDisplayConfigGetResponse> listConfigs() {
        return auditQueryProvider.getGoodsDisplayConfigForPc();
    }


    /**
     * 获取商城小程序码，与平台显示的小程序码是一样的
     * @return
     */
    @ApiOperation(value = "获取商城小程序码")
    @RequestMapping(value = "/getPublicQrcode", method = RequestMethod.GET)
    public BaseResponse<String> getPublicQrcode() {
        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
        request.setPage("pages/index/index");
        request.setScene("123");
        return wechatAuthProvider.getWxaCodeUnlimit(request);
    }
}
