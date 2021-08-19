package com.wanmi.sbc.customer.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatisticsPointsVO {

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 用户账号
     */
    private String customerAccount;

    /**
     * 积分数量
     */
    private Long points;
}
