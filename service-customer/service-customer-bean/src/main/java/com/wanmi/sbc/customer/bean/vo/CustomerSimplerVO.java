package com.wanmi.sbc.customer.bean.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerSimplerVO implements Serializable {

    private String customerId;

    private String wxMiniOpenId;
}
