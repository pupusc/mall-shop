package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 退款流水
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundFundDTO implements Serializable {

    private static final long serialVersionUID = -6868377368539328610L;

    /**
     * 退款金额，单位：元
     */
    private String refund_fee;

    /**
     * 退款流水号
     */
    private String refund_no;

    /**
     * 退款状态 0.退款中 2.退款成功
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String create_time;
}
