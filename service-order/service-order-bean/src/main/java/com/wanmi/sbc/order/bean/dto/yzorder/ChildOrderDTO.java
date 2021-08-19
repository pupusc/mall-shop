package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 子单详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildOrderDTO implements Serializable {

    private static final long serialVersionUID = 2367561932201765806L;

    /**
     * 订单号
     */
    private String tid;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 收货地址详情
     */
    private String address_detail;

    /**
     * 领取人姓名
     */
    private String user_name;

    /**
     * 领取人电话
     */
    private String user_tel;

    /**
     * 老送礼订单状态 WAIT_EXPRESS(5, "待发货"), EXPRESS(6, "已发货"), SUCCESS(100, "成功")
     */
    private String order_state;

}
