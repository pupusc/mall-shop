package com.wanmi.sbc.customer.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class IsCounselorVo implements Serializable {

    private static final long serialVersionUID = -6443795302738191541L;
    /**
     * 用户Id
     */
    private String customerId;
    /**
     * 是否是知识顾问
     */
    private Boolean isCounselor;

    /**
     * 可用知识豆
     */
    private Integer profit;
}
