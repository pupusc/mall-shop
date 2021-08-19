package com.wanmi.sbc.customer.email.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户财务邮箱response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEmailResponse {
    /**
     * 邮箱配置Id
     */
    private String customerEmailId;

    /**
     * 邮箱所属客户Id
     */
    private String customerId;

    /**
     * 发信人邮箱地址
     */
    private String emailAddress;
}
