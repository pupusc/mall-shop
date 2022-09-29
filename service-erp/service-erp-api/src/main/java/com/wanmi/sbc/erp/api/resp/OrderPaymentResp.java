package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/13 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderPaymentResp {

    /**
     * 支付流水号
     */
    private Long tid;

//    /**
//     * 支付流水-网关
//     */
//    private String payTradeNo;

    /**
     * 支付类型，1 现金，2 知豆，3 积分，4 智慧币
     */
    private String payType;

    /**
     * 支付网关
     *
     * @see com.soybean.unified.order.api.enums.PaymentGatewayEnum
     */
    private Integer payGateway;

    /**
     * 支付金额
     */
    private Integer amount;

    /**
     * 支付时间
     */
    private Date payTime;

//    /**
//     * 支付商户号
//     */
//    private String payMchid;

    /**
     * 本地订单号
     */
    private Long orderNumber;

    /**
     * 订单ID
     */
    private Long orderId;
}
