package com.wanmi.sbc.customer.api.request.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员资金-修改会员账号Request对象
 * @author chenyufei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashModifyCustomerAccountRequest implements Serializable {

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员账号
     */
    private String customerAccount;

}
