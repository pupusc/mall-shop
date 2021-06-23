package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.response.GoodsDisplayConfigGetResponse;
import com.wanmi.sbc.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 获取后台配置项
 * Created by xmn on 2018/10/10.
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private AuditQueryProvider auditQueryProvider;

    @Resource
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;


    /**
     * 前台商品列表默认展示维度（大小图、spu |sku）
     *
     * @return
     */
    @RequestMapping(value = "/goodsDisplayDefault", method = RequestMethod.GET)
    public BaseResponse<GoodsDisplayConfigGetResponse> listConfigs() {
        return auditQueryProvider.getGoodsDisplayConfigForMobile();
    }


    /**
     * 获取某个商品的小程序码
     * @return
     */
    @RequestMapping(value = "/getSkuQrCode/{skuId}", method = RequestMethod.GET)
    public BaseResponse<String> getSkuQrCode(@PathVariable String skuId) {
        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
        String url = String.format("/goods-details/%s/%s/%s", skuId, commonUtil.getOperatorId(), commonUtil.getShareId(""));
        String redisKey = (MD5Util.md5Hex(url, "utf-8")).toUpperCase().substring(16);
        if (StringUtils.isNotBlank(redisKey)) {
            redisService.setString(redisKey, url, 15000000L);
        }
        request.setPage("pages/sharepage/sharepage");
        request.setScene("NM" + redisKey);
        return wechatAuthProvider.getWxaCodeUnlimit(request);
    }
}
