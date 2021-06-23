package com.wanmi.sbc.customer.storecustomer.response;

import lombok.Data;

import java.math.BigInteger;

/**
 * Author: bail
 * Time: 2017/11/15.16:34
 */
@Data
public class StoreCustomerResponse {
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
     * 客户等级ID
     */
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    private String customerLevelName;

    /**
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public static StoreCustomerResponse convertFromNativeSQLResult(Object result) {
        StoreCustomerResponse response = new StoreCustomerResponse();
        response.setCustomerId((String) ((Object[]) result)[0]);
        response.setCustomerAccount((String) ((Object[]) result)[1]);
        response.setCustomerName((String) ((Object[]) result)[2]);
        if (((Object[]) result)[3] != null) {
            response.setCustomerLevelId(((BigInteger) ((Object[]) result)[3]).longValue());
        }
        response.setCustomerLevelName((String) ((Object[]) result)[4]);
        return response;
    }

}
