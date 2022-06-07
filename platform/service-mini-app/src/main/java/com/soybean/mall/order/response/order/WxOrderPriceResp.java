package com.soybean.mall.order.response.order;

import lombok.Data;

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
    private String originPrice;

    /**
     * 优惠券金额
     */
    private String couponPrice;

    /**
     * 积分金额
     */
    private String pointsPrice;

    /**
     * 运费
     */
    private String freightPrice;

    /**
     * 实付金额
     */
    private String actualPrice;
}
