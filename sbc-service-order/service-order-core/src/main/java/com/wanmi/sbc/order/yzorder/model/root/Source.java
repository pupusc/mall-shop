package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单来源平台
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Source implements Serializable {

    private static final long serialVersionUID = -2883735590173118756L;

    /**
     * 微信平台细分 wx_gzh:微信公众号; yzdh:有赞大号; merchant_xcx:商家小程序; yzdh_xcx:有赞大号小程序; direct_buy:直接购买
     */
    private String wx_entrance;

    /**
     * 平台 wx:微信; merchant_3rd:商家自有app; buyer_v:买家版; browser:系统浏览器; alipay:支付宝;qq:腾讯QQ; wb:微博; other:其他
     */
    private String platform;
}
