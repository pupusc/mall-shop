package com.wanmi.sbc.customer.api.request.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员资金-修改会员名称、会员账号Request对象
 * @author: Geek Wang
 * @createDate: 2019/2/25 14:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFundsModifyCustomerNameAndAccountRequest implements Serializable {

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
