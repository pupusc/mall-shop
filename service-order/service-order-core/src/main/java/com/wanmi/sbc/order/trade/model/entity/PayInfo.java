package com.wanmi.sbc.order.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单支付信息
 * @author wumeng[OF2627]
 *         company qianmi.com
 *         Date 2017-04-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayInfo implements Serializable{
    private static final long serialVersionUID = 1859928406081744303L;

    /**
     * 支付类型标识
     */
    private String payTypeId;

    /**
     * 支付类型名称
     */
    private  String payTypeName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 是否合并支付（在线支付场景）
     */
    private boolean isMergePay;

}
