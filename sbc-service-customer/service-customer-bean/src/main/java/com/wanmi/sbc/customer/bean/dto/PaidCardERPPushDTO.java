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


}
