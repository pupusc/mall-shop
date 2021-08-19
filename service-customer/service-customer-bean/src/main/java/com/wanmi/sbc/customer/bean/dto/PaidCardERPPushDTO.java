package com.wanmi.sbc.customer.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaidCardERPPushDTO {

    private String spuCode;

    private String skuCode;

    private String phone;

    private String account;

    private String price;

    private LocalDateTime payTime;

    private String payTypeCode;

    private String shopCode;

    private String vipCode;

    private String platformCode;

    //会期开始时间
    private LocalDateTime beginTime;

    //会期结束时间
    private LocalDateTime endTime;


}
