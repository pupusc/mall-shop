package com.wanmi.sbc.customer.api.request.fandeng;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FanDengWxAuthLoginRequest {

    private String unionId;
    private String openId;
    private String areaCode;
    private String mobile;
    private String registerSource;
}
