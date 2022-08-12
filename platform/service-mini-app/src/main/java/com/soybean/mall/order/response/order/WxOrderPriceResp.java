package com.soybean.mall.order.response.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 5:45 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxOrderPriceResp {

    /**
     * 商品总金额
     */
    private BigDecimal originPrice;

    /**
     * 商品优惠金额
     */
    private BigDecimal discountsPrice;

    /**
     * 会员优惠金额
     */
    private BigDecimal vipDiscountPrice;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 积分金额
     */
    private BigDecimal pointsPrice;

    /**
     * 促销价
     */
    private BigDecimal marketingPrice;

    /**
     * 运费
     */
    private BigDecimal freightPrice;

    /**
     * 实付金额
     */
    private BigDecimal actualPrice;
}
