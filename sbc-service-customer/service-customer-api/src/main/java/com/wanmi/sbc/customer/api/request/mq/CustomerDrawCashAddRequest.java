package com.wanmi.sbc.customer.api.request.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新增会员-初始化会员提现管理信息Request对象
 * @author chenyufei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashAddRequest implements Serializable {

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 会员账号
     */
    private String customerAccount;

}
