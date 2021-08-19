package com.wanmi.sbc.customer.detail.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailBase implements Serializable{

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员详情ID
     */
    private String customerDetailId;
}
