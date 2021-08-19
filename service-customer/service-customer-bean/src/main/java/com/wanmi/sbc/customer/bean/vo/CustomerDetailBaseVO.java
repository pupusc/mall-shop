package com.wanmi.sbc.customer.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailBaseVO implements Serializable{

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员详情ID
     */
    private String customerDetailId;
}
