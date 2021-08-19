package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 物流信息
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logistics {
    /**
     * 物流配送方式编号
     */
    private String shipMethodId;
    /**
     * 物流配送方式名称
     */
    private String shipMethodName;
    /**
     * 物流号
     */
    private String logisticNo;
    /**
     * 物流费
     */
    private BigDecimal logisticFee;
    /**
     * 物流公司编号
     */
    private String logisticCompanyId;
    /**
     * 物流公司名称
     */
    private String logisticCompanyName;
    /**
     * 物流公司标准编码
     */
    private String logisticStandardCode;

    /**
     * 第三方平台物流对应的订单id
     */
    private String thirdPlatformOrderId;

    /**
     * 第三方平台外部订单id
     * linkedmall --> 淘宝订单号
     */
    private String outOrderId;

    /**
     * 购买用户id
     */
    private String buyerId;

}
