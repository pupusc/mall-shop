package com.wanmi.sbc.customer.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBaseVO implements Serializable{

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 账户
     */
    private String customerAccount;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 会员等级ID
     */
    private Long customerLevelId;

    /**
     * 会员等级名称
     */
    private String customerLevelName;
}
